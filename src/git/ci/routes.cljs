(ns git.ci.routes
  (:require
   [clojure.string :as st]
   [hiccups.runtime]
   [macchiato.util.response :as r]
   [reitit.ring :as ring])

  (:require-macros
   [hiccups.core :refer (html)]))

(defn not-found [req _ _]
  (-> (html
       [:html
        [:body
         [:h2 (:uri req) " was not found"]]])
      (r/not-found)
      (r/content-type "text/html")
      #_(res)))


(defn json-serialization
  "Test 1: JSON serialization"
  [_ res _]
  (-> (js/JSON.stringify #js {:message "Hello, World!"})
      (r/ok)
      (r/content-type "application/json")
      (res)))


(defn plaintext [_ res _]
  (-> (r/ok "Hello, World!")
      (r/content-type "text/html")
      (res)))

(defn escape-html [s]
  (st/escape s
             {"&"  "&amp;"
              ">"  "&gt;"
              "<"  "&lt;"
              "\"" "&quot;"}))

(def routes
  [["/" {:name ::hello
         :get {:handler plaintext}}]
   ["/hello" {:name ::hello-path
              :get {:handler plaintext}}]])

(def router
  (ring/ring-handler
   (ring/router routes)
   (ring/create-default-handler
    {:not-found not-found})))
