(ns bam.db.datomic-adapter
  (:require [clojure.string :refer [lower-case]]
            [clojure.set :refer [rename-keys]]
            [bam.db.schema :as schema :refer [data-schema data-entities data-references data-types]]
            [datomic.api :as d]))

;; dat-keyword needs to match keyword in schema/datatypes for Datomic
(def dat-keyword :dat)
(def dat-type "db.type")
(def dat-cardinality "db.cardinality")
(def dat-unique "db.unique")

(defn uniqueness-check
  "Assoc Datomic uniqueness if it exists"
  [result uniq-attr]
  (if uniq-attr
    (assoc result :db/unique (keyword dat-unique (lower-case uniq-attr)))
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
     :db/valueType          (keyword dat-type (get-in schema/data-types [type dat-keyword]))
     :db/cardinality        (keyword dat-cardinality (lower-case cardinality))
     :db/doc                doc
     :db/index              index
     :db/fulltext           fulltext
     :db/isComponent        is-component
     :db/noHistory          no-history
     :db.install/_attribute :db.part/db}
    (uniqueness-check unique)))

(defn build-entity-sch
  "Creates Datomic schema for a single entity"
  [ent coll]
  (let [ent-as-string (name ent)]
    (map #(datomic-schema-attribute ent-as-string %) coll)))

(defn build-datomic-schema
  "Builds Datomic schema for from all entities as defined in data-schema"
  [result ent]
  (->> (ent schema/data-schema)
       (build-entity-sch ent)
       (lazy-cat result)))

;; DATOMIC- TRANSLATE
(defn set-alt-type [])
(defn ent-attrs [{:keys [id username fullname email] :as m}]
  [{:db/id         id
    :user/username username
    :user/fullName fullname
    :user/email    email}])

;; DATOMIC- IMPORT DATA
(defn set-temp-id [id]
  (d/tempid :db.part/user id))

(defn set-ref [result ref])

(defn set-ref-data
  "Checks data-references for attrs that need to be converted to ref data"
  [{:keys [ent] :as m}]
  (prn ent)
  (if-let [refs (ent schema/data-references)]
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
