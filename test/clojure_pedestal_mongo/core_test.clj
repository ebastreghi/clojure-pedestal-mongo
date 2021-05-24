(ns clojure-pedestal-mongo.core-test
  (:require [clojure.test :refer [testing deftest is]]
            [matcher-combinators.test]
            [clojure-pedestal-mongo.core :as core]
            [io.pedestal.http :as http]
            [io.pedestal.test :as http-test]
            [cheshire.core :as json]))

(defn make-request! [verb path & args]
  (let [service-fn (::http/service-fn (core/create-server))
        response (apply http-test/response-for service-fn verb path args)]
    (update response :body json/decode true)))

(deftest users-listining-and-creation
  (testing "listing users"
    (reset! core/users [])
    (is (match? {:body [] :status 200}
                (make-request! :get "/users"))))

  (testing "creating an user"
    (is (match? {:body {:name "New user" :age 20} :status 201}
                (make-request! :post "/users"
                               :headers {"Content-Type" "application/json"}
                               :body (json/encode {:name "New user" :age 20})))))

  (testing "listing users after adding one"
    (reset! core/users [])
    (is (match? {:body [{:name "New user" :age 20}] :status 200}
                (make-request! :get "/users")))))
