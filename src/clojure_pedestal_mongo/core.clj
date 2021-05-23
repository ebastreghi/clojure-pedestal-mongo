(ns clojure-pedestal-mongo.core
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defn respond-hello [request]
  {:status 200 :body "Hello Ed"})

(def routes
  (route/expand-routes
    #{["/greet" :get respond-hello :route-name :greet]}))

(defn create-server []
  (http/create-server
    {::http/routes routes
     ::http/type :jetty
     ::http/join? false?
     ::http/port 3000}))

(def server (http/start (create-server)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
