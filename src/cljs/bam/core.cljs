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

(defn nav-link [uri title page]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href uri} title]]))

(defn sidebar []
  [:div.sidebar-nav
   [:ul.nav
    [nav-link "#/" "Home" :home]
    [nav-link "#/about" "About" :about]]])

(defn navbar []
  [:div.navbar.navbar-default.navbar-fixed-top.hidden-md-up
   {:role "navigation"}
   [:div.container-fluid
    [:div.navbar-header
     [:button.btn.btn-lg.navbar-toggler.hidden-md-up.pull-right
      {:on-click #(rf/dispatch [:toggle-sidebar])}
      "☰"]
     [:a.navbar-brand {:href "#/"} "BAM"]]]])

(defn jumbotron []
  [:div.jumbotron
   [:h1 "Welcome to bam"]
   [:p "Time to start building your site!"]
   [:p [:a.btn.btn-primary.btn-lg {:href "http://luminusweb.net"} "Learn more »"]]])

(defn about-page []
  [:div.row
   [:div.col-md-12
    "this is the story of bam... work in progress"]])

(defn welcome-banner []
  [:div.row
   [:div.col-md-12
    [:h2 "Welcome to ClojureScript"]]])

(defn doc-links []
  (when-let [docs @(rf/subscribe [:docs])]
    [:div.row
     [:div.col-md-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]]]))

(defn home-page []
  [:div.row
   (jumbotron)
   (welcome-banner)
   (doc-links)])

(def pages
  {:home  #'home-page
   :about #'about-page})

(defn page []
  [:div
   [:section (navbar)]
   [:section#main-section
    [:div.row.row-offcanvas
     {:class @(rf/subscribe [:sidebar-state])}
     [:div#sidebar.col-xs-6.col-sm-3.col-md-2.sidebar-offcanvas
      {:role "navigation"} (sidebar)]
     [:div.col-xs-12.col-sm-9.col-md-10
      [(pages @(rf/subscribe [:page]))]]]]])

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
