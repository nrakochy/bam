(ns bam.db.schema
  (:require [clojure.string :refer [lower-case]]))

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
   :db/valueType (keyword "db.type" ((keyword (lower-case type)) data-types))
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
