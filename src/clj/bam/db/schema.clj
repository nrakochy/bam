(ns bam.db.schema
  (:require [clojure.string :refer [lower-case]]
            [schema.core :as s]))

;; dynamic symbols-> prefix needs to match required schema.core :as value
(def sch-prefix "s")
(def ^:dynamic ent (symbol "Schema"))

;;
(def data-schema
  [{:ns         "user"
   :ident       "first-name"
   :type        "string"
   :cardinality "one"
   :doc         "User's first name"
   :optional    true ;; Required for Schema not Datomic
  }])

(def data-types
  ;; Prismatic Schema (:sch) types - Any, Bool, Num, Keyword, Symbol, Int, and Str
  ;; :dat refers to Datomic types
    {:string    {:dat "string" :sch "Str"}
    :boolean    {:dat "boolean" :sch "Bool"}
    :long       {:dat "long" :sch "Num"}
    :bigint     {:dat "bigint" :sch "Int"}
    :float      {:dat "float" :sch "Num"}
    :double     {:dat "double" :sch "Num" }
    :bigdec     {:dat "bigdec" :sch "Num"}
    :ref        {:dat "ref" :sch "Ref"}
    :instant    {:dat "instant" :sch "Any"}
    :uuid       {:dat  "uuid" :sch "Any"}
    :uri        {:dat "uri" :sch "Any"}
    :bytes      {:dat "byte" :sch "Any"}})


;; DATOMIC
(defn datomic-schema-attribute
  "Build a datomic schema attribute. Required keys with string values- :ns :ident :type :cardinality (one or many)"
  [{:keys [ns ident type cardinality index doc unique fulltext is-component no-history]
    :or {index true doc "No docs provided" unique false fulltext false is-component false no-history false}}]
    {:db/ident (keyword ns ident)
     :db/valueType (keyword "db.type" (get-in data-types [(keyword (lower-case type)) :dat]))
     :db/cardinality (keyword "db.cardinality" (lower-case cardinality))
     :db/doc doc
     :db/index index
     :db/unique unique
     :db/fulltext fulltext
     :db/isComponent is-component
     :db/noHistory no-history
     :db.install/_attribute :db.part/db})

(defn build-datomic-schema [coll] (map datomic-schema-attribute coll))

;; SCHEMA
(defn filter-by-ns
  "Filters map based on given ent-ns value"
  [m ent-ns]
  (filter #(= ent-ns %) m))

(defn build-entity-attr
  "Takes a result map and a schema map to assoc required field or not"
  [m & [{:keys [ns ident type optional] :or [optional true]}]]
    (let [req #(s/optional-key %)]
    (let [sch-type (symbol sch-prefix (get-in data-types [(keyword (lower-case type)) :sch]))]
      (if optional
        (assoc m (req (keyword ident)) sch-type)
        (assoc m (keyword ident) sch-type)))))

(defn build-schema-lib
   [{:keys [ns ident type]}]
     (binding [ent (symbol ns)]
       (s/defschema ent
         {(keyword ident)(symbol sch-prefix (get-in data-types [(keyword (lower-case type)) :sch]))
          (s/optional-key :description) s/Str})
      (prn (class ent))
       ent))
