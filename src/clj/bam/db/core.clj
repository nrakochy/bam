(ns bam.db.core
  (:require [datomic.api :as d]
            [mount.core :as mount :refer [defstate]]
            [bam.config :refer [env]]
            [bam.db.datomic-adapter :as db-schema :refer [datomic-schema]]))

(mount/defstate db-conn
                :start (-> env :database-url d/connect)
                :stop (-> db-conn .release))

(defn db-inst []
  (d/db db-conn))

(defn create-schema [conn]
  @(d/transact conn datomic-schema))

(defn add-user
  [conn {:keys [id username fullname email role]}]
  @(d/transact conn [{:db/id         (d/tempid :db.part/user id)
                      :user/username username
                      :user/fullName fullname
                      :enum/role     role
                      :user/email    email}]))

;; inspiratiton- https://gist.github.com/bobby/94047e1bb08760a1cf71
(defn retrieve-with
  "Logical OR of given hash-map
   e.g. {:user/username 'test' :user/email 'test@example.com'}
   will return users with either of those attrs"
  ([m] (retrieve-with (db-inst) m))
  ([db m]
   (prn (str "Retrieving entity with attributes: " m))
   (d/q '[:find [(pull ?e [*])]
          :in $ [[?a ?v]]
          :where [?e ?a ?v]]
        db m)))

(defn sample-data [] [
                  {:id -1 :ent :user :username "nrako" :email "test@example.com" :fullname "Nick" :role -4}
                  {:id -2 :ent :user :username "test8" :email "test8@example.com" :fullname "Test Name"}
                  {:id -4 :ent :enum :role "Developer"}
                  ])
