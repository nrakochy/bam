([:html
  {:lang "en"}
  [:head
   [:meta
    {:content "text/html; charset=UTF-8", :http-equiv "Content-Type"}]
   [:meta {:charset "utf-8"}]
   [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
   [:meta
    {:content "width=device-width, initial-scale=1", :name "viewport"}]
   [:title "BAM!"]
   [:link {:rel "stylesheet", :href "../build/css/custom.min.css"}]]
  [:body.nav-md
   [:div.container.body
    [:div.main_container
     [:div.col-md-3.left_col
      [:div.left_col.scroll-view
       [:div.navbar.nav_title
        {:style "border: 0;"}
        [:a.site_title
         {:href "index.html"}
         [:i.fa.fa-paw]
         [:span "Gentellela Alela!"]]]
       [:div.clearfix]
       [:div.profile
        [:div.profile_pic
         [:img.img-circle.profile_img
          {:alt "...", :src "images/img.jpg"}]]
        [:div.profile_info [:span "Welcome,"] [:h2 "John Doe"]]]
       [:br]
       [:div#sidebar-menu.main_menu_side.hidden-print.main_menu
        [:div.menu_section
         [:h3 "General"]
         [:ul.nav.side-menu
          [:li
           [:a [:i.fa.fa-home] " Home " [:span.fa.fa-chevron-down]]
           [:ul.nav.child_menu
            [:li [:a {:href "index.html"} "Dashboard"]]
            [:li [:a {:href "index2.html"} "Dashboard2"]]
            [:li [:a {:href "index3.html"} "Dashboard3"]]]]
          [:li
           [:a [:i.fa.fa-edit] " Forms " [:span.fa.fa-chevron-down]]
           [:ul.nav.child_menu
            [:li [:a {:href "form.html"} "General Form"]]
            [:li
             [:a {:href "form_advanced.html"} "Advanced Components"]]
            [:li [:a {:href "form_validation.html"} "Form Validation"]]
            [:li [:a {:href "form_wizards.html"} "Form Wizard"]]
            [:li [:a {:href "form_upload.html"} "Form Upload"]]
            [:li [:a {:href "form_buttons.html"} "Form Buttons"]]]]
          [:li
           [:a
            [:i.fa.fa-desktop]
            " UI Elements "
            [:span.fa.fa-chevron-down]]
           [:ul.nav.child_menu
            [:li
             [:a {:href "general_elements.html"} "General Elements"]]
            [:li [:a {:href "media_gallery.html"} "Media Gallery"]]
            [:li [:a {:href "typography.html"} "Typography"]]
            [:li [:a {:href "icons.html"} "Icons"]]
            [:li [:a {:href "glyphicons.html"} "Glyphicons"]]
            [:li [:a {:href "widgets.html"} "Widgets"]]
            [:li [:a {:href "invoice.html"} "Invoice"]]
            [:li [:a {:href "inbox.html"} "Inbox"]]
            [:li [:a {:href "calendar.html"} "Calendar"]]]]
          [:li
           [:a [:i.fa.fa-table] " Tables " [:span.fa.fa-chevron-down]]
           [:ul.nav.child_menu
            [:li [:a {:href "tables.html"} "Tables"]]
            [:li [:a {:href "tables_dynamic.html"} "Table Dynamic"]]]]
          [:li
           [:a
            [:i.fa.fa-bar-chart-o]
            " Data Presentation "
            [:span.fa.fa-chevron-down]]
           [:ul.nav.child_menu
            [:li [:a {:href "chartjs.html"} "Chart JS"]]
            [:li [:a {:href "chartjs2.html"} "Chart JS2"]]
            [:li [:a {:href "morisjs.html"} "Moris JS"]]
            [:li [:a {:href "echarts.html"} "ECharts"]]
            [:li [:a {:href "other_charts.html"} "Other Charts"]]]]
          [:li
           [:a [:i.fa.fa-clone] "Layouts " [:span.fa.fa-chevron-down]]
           [:ul.nav.child_menu
            [:li [:a {:href "fixed_sidebar.html"} "Fixed Sidebar"]]
            [:li [:a {:href "fixed_footer.html"} "Fixed Footer"]]]]]]
        [:div.menu_section
         [:h3 "Live On"]
         [:ul.nav.side-menu
          [:li
           [:a
            [:i.fa.fa-bug]
            " Additional Pages "
            [:span.fa.fa-chevron-down]]
           [:ul.nav.child_menu
            [:li [:a {:href "e_commerce.html"} "E-commerce"]]
            [:li [:a {:href "projects.html"} "Projects"]]
            [:li [:a {:href "project_detail.html"} "Project Detail"]]
            [:li [:a {:href "contacts.html"} "Contacts"]]
            [:li [:a {:href "profile.html"} "Profile"]]]]
          [:li
           [:a
            [:i.fa.fa-windows]
            " Extras "
            [:span.fa.fa-chevron-down]]
           [:ul.nav.child_menu
            [:li [:a {:href "page_403.html"} "403 Error"]]
            [:li [:a {:href "page_404.html"} "404 Error"]]
            [:li [:a {:href "page_500.html"} "500 Error"]]
            [:li [:a {:href "plain_page.html"} "Plain Page"]]
            [:li [:a {:href "login.html"} "Login Page"]]
            [:li [:a {:href "pricing_tables.html"} "Pricing Tables"]]]]
          [:li
           [:a
            [:i.fa.fa-sitemap]
            " Multilevel Menu "
            [:span.fa.fa-chevron-down]]
           [:ul.nav.child_menu
            [:li [:a {:href "#level1_1"} "Level One"]]
            [:li
             [:a "Level One" [:span.fa.fa-chevron-down]]
             [:ul.nav.child_menu
              [:li.sub_menu [:a {:href "level2.html"} "Level Two"]]
              [:li [:a {:href "#level2_1"} "Level Two"]]
              [:li [:a {:href "#level2_2"} "Level Two"]]]]
            [:li [:a {:href "#level1_2"} "Level One"]]]]
          "                  \n                  "
          [:li
           [:a
            {:href "javascript:void(0)"}
            [:i.fa.fa-laptop]
            " Landing Page "
            [:span.label.label-success.pull-right "Coming Soon"]]]]]]
       [:div.sidebar-footer.hidden-small
        [:a
         {:title          "Settings",
          :data-placement "top",
          :data-toggle    "tooltip"}
         [:span.glyphicon.glyphicon-cog {:aria-hidden "true"}]]
        [:a
         {:title          "FullScreen",
          :data-placement "top",
          :data-toggle    "tooltip"}
         [:span.glyphicon.glyphicon-fullscreen {:aria-hidden "true"}]]
        [:a
         {:title "Lock", :data-placement "top", :data-toggle "tooltip"}
         [:span.glyphicon.glyphicon-eye-close {:aria-hidden "true"}]]
        [:a
         {:title          "Logout",
          :data-placement "top",
          :data-toggle    "tooltip"}
         [:span.glyphicon.glyphicon-off {:aria-hidden "true"}]]]]]
     [:div.top_nav
      [:div.nav_menu
       [:nav
        [:div.nav.toggle [:a#menu_toggle [:i.fa.fa-bars]]]
        [:ul.nav.navbar-nav.navbar-right
         [:li
          [:a.user-profile.dropdown-toggle
           {:aria-expanded "false",
            :data-toggle   "dropdown",
            :href          "javascript:;"}
           [:img {:alt "", :src "images/img.jpg"}]
           "John Doe\n                    "
           [:span.fa.fa-angle-down]]
          [:ul.dropdown-menu.dropdown-usermenu.pull-right
           [:li [:a {:href "javascript:;"} " Profile"]]
           [:li
            [:a
             {:href "javascript:;"}
             [:span.badge.bg-red.pull-right "50%"]
             [:span "Settings"]]]
           [:li [:a {:href "javascript:;"} "Help"]]
           [:li
            [:a
             {:href "login.html"}
             [:i.fa.fa-sign-out.pull-right]
             " Log Out"]]]]
         [:li.dropdown
          {:role "presentation"}
          [:a.dropdown-toggle.info-number
           {:aria-expanded "false",
            :data-toggle   "dropdown",
            :href          "javascript:;"}
           [:i.fa.fa-envelope-o]
           [:span.badge.bg-green "6"]]
          [:ul#menu1.dropdown-menu.list-unstyled.msg_list
           {:role "menu"}
           [:li
            [:a
             [:span.image
              [:img {:alt "Profile Image", :src "images/img.jpg"}]]
             [:span [:span "John Smith"] [:span.time "3 mins ago"]]
             [:span.message
              "\n                          Film festivals used to be do-or-die moments for movie makers. They were where...\n                        "]]]
           [:li
            [:a
             [:span.image
              [:img {:alt "Profile Image", :src "images/img.jpg"}]]
             [:span [:span "John Smith"] [:span.time "3 mins ago"]]
             [:span.message
              "\n                          Film festivals used to be do-or-die moments for movie makers. They were where...\n                        "]]]
           [:li
            [:a
             [:span.image
              [:img {:alt "Profile Image", :src "images/img.jpg"}]]
             [:span [:span "John Smith"] [:span.time "3 mins ago"]]
             [:span.message
              "\n                          Film festivals used to be do-or-die moments for movie makers. They were where...\n                        "]]]
           [:li
            [:a
             [:span.image
              [:img {:alt "Profile Image", :src "images/img.jpg"}]]
             [:span [:span "John Smith"] [:span.time "3 mins ago"]]
             [:span.message
              "\n                          Film festivals used to be do-or-die moments for movie makers. They were where...\n                        "]]]
           [:li
            [:div.text-center
             [:a
              [:strong "See All Alerts"]
              [:i.fa.fa-angle-right]]]]]]]]]]
     [:div.right_col {:role "main"}]
     [:footer
      [:div.pull-right
       "\n            Gentelella - Bootstrap Admin Template by "
       [:a {:href "https://colorlib.com"} "Colorlib"]]
      [:div.clearfix]]]]]])
