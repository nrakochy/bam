(ns bam.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [bam.ajax :refer [load-interceptors!]]
            [bam.handlers]
            [bam.subscriptions])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])] [:li.nav-item
   {:class (when (= page @selected-page) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(reset! collapsed? true)} title]]))

(defn sidebar-search-input []
  [:div.input-group.custom-search-form
       [:input.form-control {:placeholder "Search...", :type "text"}]
       [:span.input-group-btn
        [:button.btn.btn-default {:type "button"} [:i.fa.fa-search]]]])
  
(defn sidebar []
  [:div.navbar-default.sidebar
   {:role "navigation"}
   [:div.sidebar-nav.navbar-collapse
    [:ul#side-menu.nav
     [:li.sidebar-search
       (sidebar-search-input)]
     [:li
      [:a
       {:href "index.html"}
       [:i.fa.fa-dashboard.fa-fw]
       " Dashboard"]]
     [:li
      [:a
       {:href "#"}
       [:i.fa.fa-files-o.fa-fw]
       " Sample Pages"
       [:span.fa.arrow]]
      [:ul.nav.nav-second-level
       [:li [:a {:href "blank.html"} "Blank Page"]]
       [:li [:a {:href "login.html"} "Login Page"]]]]]]])

(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:nav.navbar.navbar-light.bg-faded
     [:button.navbar-toggler.hidden-sm-up
      {:on-click #(swap! collapsed? not)} "☰"]
     [:div.collapse.navbar-toggleable-xs
      (when-not @collapsed? {:class "in"})
      [:a.navbar-brand {:href "#/"} "bam"]
      [:ul.nav.navbar-nav
       [nav-link "#/" "Home" :home collapsed?]
       [nav-link "#/about" "About" :about collapsed?]]]
     (sidebar)
       ]))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     "this is the story of bam... work in progress"]]])

(defn home-page []
  [:div.container
   [:div.jumbotron
    [:h1 "Welcome to bam"]
    [:p "Time to start building your site!"]
    [:p [:a.btn.btn-primary.btn-lg {:href "http://luminusweb.net"} "Learn more »"]]]
   [:div.row
    [:div.col-md-12
     [:h2 "Welcome to ClojureScript"]]]
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row
      [:div.col-md-12
       [:div {:dangerouslySetInnerHTML
              {:__html (md->html docs)}}]]])])

(def pages
  {:home #'home-page
   :about #'about-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET (str js/context "/docs") {:handler #(rf/dispatch [:set-docs %])}))

(defn mount-components []
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))


(comment
  [:nav {:class "navbar navbar-default navbar-static-top", :role "navigation", :style "margin-bottom: 0"}
 [:div {:class "navbar-header"}
  [:button {:type "button", :class "navbar-toggle", :data-toggle "collapse", :data-target ".navbar-collapse"}
   [:span {:class "sr-only"} "Toggle navigation"]
   [:span {:class "icon-bar"}]
   [:span {:class "icon-bar"}]
   [:span {:class "icon-bar"}]]
  [:a {:class "navbar-brand", :href "index.html"} "BAM"]]
 [:ul {:class "nav navbar-top-links navbar-right"}
  [:li {:class "dropdown"}
   [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"}
    [:i {:class "fa fa-user fa-fw"}]
    [:i {:class "fa fa-caret-down"}]]
   [:ul {:class "dropdown-menu dropdown-user"}
    [:li 
     [:a {:href "#"}
      [:i {:class "fa fa-user fa-fw"}]" User Profile"]]
    [:li 
     [:a {:href "#"}
      [:i {:class "fa fa-gear fa-fw"}]" Settings"]]
    [:li {:class "divider"}]
    [:li 
     [:a {:href "login.html"}
      [:i {:class "fa fa-sign-out fa-fw"}]" Logout"]]]]]
 [:div {:class "navbar-default sidebar", :role "navigation"}
  [:div {:class "sidebar-nav navbar-collapse"}
   [:ul {:class "nav", :id "side-menu"}
    [:li {:class "sidebar-search"}
     [:div {:class "input-group custom-search-form"}
      [:input {:type "text", :class "form-control", :placeholder "Search..."}]
      [:span {:class "input-group-btn"}
       [:button {:class "btn btn-default", :type "button"}
        [:i {:class "fa fa-search"}]]]]"<!-- /input-group -->" ]
    [:li  
     [:a {:href "index.html"}
      [:i {:class "fa fa-dashboard fa-fw"}]" Dashboard"]]
    [:li  
     [:a {:href "#"}
      [:i {:class "fa fa-files-o fa-fw"}]" Sample Pages" 
      [:span {:class "fa arrow"}]]
     [:ul {:class "nav nav-second-level"}
      [:li  
       [:a {:href "blank.html"} "Blank Page"]]
      [:li  
       [:a {:href "login.html"} "Login Page"]]]]]]]]
       )