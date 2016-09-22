(ns bam.db.core
  (:require [datomic.api :as d]
            [mount.core :refer [defstate]]
            [clojure.string :as str]
            [bam.config :refer [env]]))

(defstate conn
          :start (-> env :database-url d/connect)
          :stop (-> conn .release))

(def data-types
    {:string "string"
    :boolean "boolean"
    :long "long"
    :bigint "bigint"
    :float  "float"
    :double "double"
    :bigdec "bigdec"
    :ref "ref"
    :instant "instant"
    :uuid "uuid"
    :uri "uri"
    :bytes "byte"})

(def data-scheme
  [{:ns         "user"
   :ident       "first-name"
   :type        "string"
   :cardinality "one"
   :doc         "User's first name"
   :index       true
   :optional    false
  }])


(defn datomic-schema-attribute
  "Build a datomic schema attribute. Required keys with string values- :ns :ident :type :cardinality (one or many)"
  [{:keys [ns ident type cardinality index doc unique fulltext is-component no-history]
    :or {index true doc "No docs provided" unique false fulltext false is-component false no-history false}}]
  {:db/ident (keyword ns ident)
   :db/valueType (keyword "db.type" ((keyword (str/lower-case type)) data-types))
   :db/cardinality (keyword "db.cardinality" cardinality)
   :db/doc doc
   :db/index index
   :db/unique unique
   :db/fulltext fulltext
   :db/isComponent is-component
   :db/noHistory no-history
   :db.install/_attribute :db.part/db})

(defn build-datomic-schema [coll] (map datomic-schema-attribute coll))

(comment
(defn schema-lib []
  (s/defschema Pizza
    {:name s/Str
     (s/optional-key :description) s/Str
     :size (s/enum :L :M :S)
     :origin {:country (s/enum :FI :PO)
              :city s/Str}}))
)

(defn create-schema []
  @(d/transact conn (build-datomic-schema data-scheme)))

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
