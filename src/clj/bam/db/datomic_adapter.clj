(ns bam.db.datomic-adapter
  (:require [clojure.string :refer [lower-case]]
            [clojure.set :refer [rename-keys]]
            [bam.db.schema :as schema :refer [data-schema data-entities data-references data-types]]
            [datomic.api :as d]))

;; Needs to match keyword in schema/datatypes for Datomic
(def dat-keyword :dat)

(defn uniqueness-check [result uniq-attr]
  (if uniq-attr
    (assoc result :db/unique (keyword "db.unique" (lower-case uniq-attr)))
    result))

(defn datomic-schema-attribute
  "Build a datomic schema attribute.
   Requires 1) Ent as string (e.g. 'user' or 'org')
            2) Map with required keys (string values)- :ident :cardinality (one or many)
                        required keys (keyword values)- :type"
  [ent {:keys [ident type cardinality unique index doc fulltext is-component no-history]
        :or   {index true doc "No docs provided" fulltext false unique nil is-component false no-history false}}]
  (->
    {:db/id                 (d/tempid :db.part/db)
     :db/ident              (keyword ent ident)
     :db/valueType          (keyword "db.type" (get-in schema/data-types [type dat-keyword]))
     :db/cardinality        (keyword "db.cardinality" (lower-case cardinality))
     :db/doc                doc
     :db/index              index
     :db/fulltext           fulltext
     :db/isComponent        is-component
     :db/noHistory          no-history
     :db.install/_attribute :db.part/db}
    (uniqueness-check unique)))

(defn build-entity-attrs [ent coll]
  (let [ent-as-string (name ent)]
    (map #(datomic-schema-attribute ent-as-string %) coll)))

(defn build-datomic-schema [result ent]
  (->>  (ent schema/data-schema)
        (build-entity-attrs ent)
        (lazy-cat result)))

;; DATOMIC- IMPORT DATA
(defn set-temp-id [id]
  (d/tempid :db.part/user id))

(defn set-ref-data [{:keys [ent] :as m}]
  (if-let [refs (ent data-references)]
    (reduce #(update %1 %2 set-temp-id) m refs)
    m))

(defn transform-into-datomic-ids
  "Returns map with transformed id (as :db/id) key + temp id value (as required by Datomic),
   ref id + related value if it exists for mapping on import"
  [{:keys [id] :as m}]
  (let [dat-id :db/id]
    (-> (rename-keys m {:id dat-id})
        (assoc dat-id (set-temp-id id))
        (set-ref-data))))

(def datomic-schema (reduce build-datomic-schema [] schema/data-entities))
