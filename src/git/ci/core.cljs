(ns git.ci.core
  (:require
   [git.ci.middleware]
   [git.ci.routes :refer (router)]
   [macchiato.http.request :as request]
   [macchiato.server :as http]))

(def config {:host "127.0.0.1"
             :port 8081})

(defonce server (atom nil))

(defn start! []
  (when-not @server
    (let [server-config
          (merge {:on-success #(.log js/console
                                     "Server started on localhost:8081" )
                  :handler (git.ci.middleware/wrap-defaults router)}
                 config)
          http-server (http/start server-config)]
      (reset! server http-server)
      server)))

(defn stop! []
  (when-let [s @server]
    (println "Stopping server")
    (.close s)
    (.unref s)
    (reset! server nil)))

(defn restart! []
  (stop!)
  (start!))

(defn main []
  (start!)
  (println "Hello"))

(defn ^:dev/after-load reload []
  (println "reload"))
