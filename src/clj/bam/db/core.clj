(ns bam.db.core
  (:require [datomic.api :as d]
            [mount.core :refer [defstate]]
            [bam.db.schema :refer [datomic-schema]]
            [bam.config :refer [env]]))

(defstate conn
          :start (-> env :database-url d/connect)
          :stop (-> conn .release))

(def db-uri-base "datomic:mem://")

(defn dev-conn []
  (let [uri (str db-uri-base (d/squuid))]
    (d/delete-database uri)
    (d/create-database uri)
    (d/connect uri)))

(def db-inst (dev-conn))

(defn create-schema []
    @(d/transact db-inst datomic-schema))

(defn entity [conn id]
  (d/entity (d/db conn) id))

(defn touch [conn results]
  "takes 'entity ids' results from a query
    e.g. '#{[272678883689461] [272678883689462] [272678883689459] [272678883689457]}'"
  (let [e (partial entity conn)]
    (map #(-> % first e d/touch) results)))

(defn add-user [conn {:keys [id first-name last-name email]}]
  @(d/transact conn [{:db/id           id
                      :user/first-name first-name
                      :user/last-name  last-name
                      :user/email      email}]))

(defn find-user [conn id]
  (let [user (d/q '[:find ?e :in $ ?id
                      :where [?e :user/id ?id]]
                    (d/db conn) id)]
    (touch conn user)))





