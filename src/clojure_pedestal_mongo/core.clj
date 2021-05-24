(ns clojure-pedestal-mongo.core
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defn respond-hello [request]
  {:status 200 :body "Hello Ed"})

;after recover this data from DB
(def users [{:name "Carlos" :age 10}
            {:name "Ana" :age 11}
            {:name "Mario" :age 12}])

(defn filter-users [params users]
  (filter (fn [user] (= params (select-keys user (keys params)))) users))

(defn get-users-handler [request]
  (-> request
      (:params {})
      (filter-users users)
      http/json-response))

(def routes
  (route/expand-routes
    #{["/greet" :get respond-hello :route-name :greet]
      ["/users" :get get-users-handler :route-name :users]}))

(def pedestal-config
  {::http/routes routes
   ::http/type :jetty
   ::http/join? false?
   ::http/port 3000})

(def server (http/start (http/create-server pedestal-config)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
