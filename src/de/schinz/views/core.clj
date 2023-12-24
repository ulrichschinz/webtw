(ns de.schinz.views.core
  (:require [hiccup.page :as page]
            [hiccup.core :as h]
            [de.schinz.tw-backend.core :as twb]))

(defn head []
  [:head
     [:title (str "Web TW")]
     (page/include-css "/css/style.css")
     (page/include-js "/js/htmx.min.js")
     [:script "htmx.logAll();"]
     [:link {:rel "shortcut icon" :href "/favicon.png"}]])

(defn page-nav []
  [:nav
   [:a {:href "/" :id "home"}
    [:div {:id "title"}
     [:image {:src "/img/webtw-logo.jpeg" :width 30 :height 30}][:h2 "WebTW"]]]
   [:div {:id "navitems"}
    [:ul
     [:li
      [:a { :href "/tasks/new"} "New Task"]]
     [:li
      [:a { :href "/tasks"} "Tasks"]]
     [:li
      [:a { :href "/tasks/inbox"} "Inbox"]]
     [:li "item one"]]]])

(defn main [content]
  [:main content])

(defn page-content []
  [:div {:id "main"}
         [:h2 "Main page"]
         [:p "Lorem ipsum dolor sit amet consectetur adipisicing elit. Labore tempora totam temporibus sint. Consequatur, laborum nisi necessitatibus deserunt ad recusandae voluptas maiores molestiae eveniet tenetur, facere quisquam quidem quia rerum!    
             Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus suscipit nisi tempora reprehenderit eaque enim cupiditate explicabo deserunt aliquam officiis rerum sapiente aperiam voluptate laborum, a ex, voluptates debitis beatae."]])


(defn home [r]
  (page/html5
    (head)
    (page-nav)
    (main (page-content))))

(defn page [content]
  (page/html5
    (head)
    (page-nav)
    (main content)))
