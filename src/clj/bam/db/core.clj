(ns bam.db.core
  (:require [datomic.api :as d]
            [bam.db.datomic-adapter :as db-schema :refer [datomic-schema]]))

(def db-uri-base "datomic:mem://")

(defn dev-conn []
  (let [uri (str db-uri-base (d/squuid))]
    (d/delete-database uri)
    (d/create-database uri)
    (d/connect uri)))

(defn db-inst
  ([] db-inst dev-conn)
  ([conn] d/db conn))

(defn create-schema []
  @(d/transact db-inst datomic-schema))

(defn entity-wrap [conn id]
  (d/entity (d/db conn) id))

(defn realize [conn results]
  "takes 'entity ids' results from a query
    e.g. '#{[272678883689461] [272678883689462] [272678883689459] [272678883689457]}'"
  (let [e (partial entity-wrap conn)]
    (map #(-> % first e d/touch) results)))

(defn add-user
  [conn {:keys [id username fullname email]}]
  @(d/transact conn [{:db/id         id
                      :user/username username
                      :user/fullName fullname
                      :user/email    email}]))

(defn transform-req [m])

(defn retrieve-entity [m]
  (-> (transform-req m)
      (retrieve m)))

;; source- https://gist.github.com/bobby/94047e1bb08760a1cf71
(defn retrieve
  "Logical OR of given hash-map
   e.g. {:user/username 'test' :user/fullName 'test1'}
   will return users with either of those attrs"
  ([m] retrieve-entity db-inst m)
  ([db m]
  (d/q '[:find ?e
         :in $ [[?a ?v]]
         :where [?e ?a ?v]]
       db m)))

(def test1 {:id 1 :username "nrako" :email "test@example.com" :fullname "Nick"})
