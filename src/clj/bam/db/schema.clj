(ns bam.db.schema
  (:require [clojure.string :refer [lower-case]]
            [clojure.set :refer [rename-keys]]
            [datomic.api :as d]
            [schema.core :as s]))

;; dynamic symbols-> sch-prefix needs to match required schema.core :as value e.g. [schema.core :as s]
(def sch-prefix "s")
(def ^:dynamic sch-name (symbol "Schema"))

(def data-schema
  ;;:optional flag is for Schema not Datomic
  [
   ;; USER
   {:ent         "user"
    :ident       "username"
    :type        "string"
    :cardinality "one"
    :unique      "value"
    :doc         "Username"
    :optional    false}
   {:ent         "user"
    :ident       "fullName"
    :type        "string"
    :cardinality "one"
    :doc         "Name by which user wants to be known"
    :optional    false}
   {:ent         "user"
    :ident       "email"
    :type        "string"
    :unique      "value"
    :cardinality "many"
    :doc         "Email accounts of users"
    :optional    false}
   {:ent         "user"
    :ident       "bio"
    :type        "string"
    :cardinality "one"
    :doc         "User bio"
    :optional    true}
   {:ent         "user"
    :ident       "company"
    :type        "string"
    :cardinality "one"
    :doc         "User Company"
    :optional    true}
   {:ent         "user"
    :ident       "roles"
    :type        "ref"
    :cardinality "many"
    :doc         "Role in project"
    :optional    true}
   {:ent         "user"
    :ident       "authentications"
    :type        "ref"
    :cardinality "many"
    :doc         "Authentication services"
    :optional    true}
   ;; ORGANIZATIONS
   {:ent         "org"
    :ident       "name"
    :type        "string"
    :cardinality "one"
    :doc         "Organization name"
    :optional    false}
   {:ent         "org"
    :ident       "category"
    :type        "ref"
    :cardinality "one"
    :doc         "Organization category"
    :optional    false}
   {:ent         "org"
    :ident       "website"
    :type        "uri"
    :cardinality "one"
    :doc         "Organization website"
    :optional    false}
   {:ent         "org"
    :ident       "ein"
    :type        "string"
    :cardinality "one"
    :doc         "Organization IRS employment id number"
    :optional    false}
   ;; PROJECT
   {:ent         "project"
    :ident       "title"
    :type        "string"
    :cardinality "one"
    :doc         "Project Name"
    :optional    false}
   {:ent         "project"
    :ident       "description"
    :type        "string"
    :cardinality "one"
    :doc         "Details of project"
    :optional    false}
   {:ent         "project"
    :ident       "type"
    :type        "string"
    :cardinality "one"
    :doc         "Type of project - on-going, feature development, start-up, etc."
    :optional    false}
   {:ent         "project"
    :ident       "workScopes"
    :type        "ref"
    :cardinality "many"
    :doc         "Details of project"
    :optional    false}
   ;; WORK SCOPE
   {:ent         "workScope"
    :ident       "skills"
    :type        "ref"
    :cardinality "many"
    :doc         "Cf. of skills needed for the project - consultative, technological, data science-ing etc."
    :optional    true}
   {:ent         "workScope"
    :ident       "description"
    :type        "string"
    :cardinality "one"
    :doc         "Description of scope of work"
    :optional    false}
   {:ent         "workScope"
    :ident       "budgetedHours"
    :type        "float"
    :cardinality "one"
    :doc         "Requested time commitment"
    :optional    false}
   ;; ENUMS (?)
   {:ent         "enum"
    :ident       "role"
    :type        "string"
    :cardinality "one"
    :doc         "Role on the project - Developer, Owner, Requester, etc."
    :optional    false}
   ;; Category
   {:ent         "category"
    :ident       "name"
    :type        "string"
    :cardinality "one"
    :doc         "Org Categorization - Community Dev, Education, etc."
    :optional    false}
   {:ent         "category"
    :ident       "cause"
    :type        "string"
    :cardinality "one"
    :doc         "Sub-category of organization - Education -> early childhood, scholarship, youth ed, etc. "
    :optional    false}
   ;; SKILLS
   {:ent         "skill"
    :ident       "name"
    :type        "string"
    :cardinality "one"
    :doc         "Skill Name - Clojure, Postgresql, Ruby, Wordpress, etc."
    :optional    true}
   {:ent         "skill"
    :ident       "type"
    :type        "string"
    :cardinality "one"
    :doc         "Type of skill - technology, project management, design, etc."
    :optional    false}
   ;; AUTHENTICATION
   {:ent         "authentication"
    :ident       "type"
    :type        "string"
    :cardinality "one"
    :doc         "Type of auth service"
    :optional    false}
   {:ent         "authentication"
    :ident       "name"
    :type        "string"
    :cardinality "one"
    :doc         "Name of auth service - Githubs, Linkedin, Facebooks, etc."
    :optional    false}
   {:ent         "authentication"
    :ident       "token"
    :type        "string"
    :cardinality "one"
    :doc         "Logging it in with a token"
    :optional    false}
   ])

(def data-types
  ;; Prismatic Schema (:sch) types - Any, Bool, Num, Keyword, Symbol, Int, and Str
  ;; :dat refers to Datomic types
  {:string  {:dat "string" :sch "Str"}
   :boolean {:dat "boolean" :sch "Bool"}
   :keyword {:dat "keyword" :sch "Any"}
   :long    {:dat "long" :sch "Num"}
   :bigint  {:dat "bigint" :sch "Int"}
   :float   {:dat "float" :sch "Num"}
   :double  {:dat "double" :sch "Num"}
   :bigdec  {:dat "bigdec" :sch "Num"}
   :ref     {:dat "ref" :sch "Any"}
   :instant {:dat "instant" :sch "Any"}
   :uuid    {:dat "uuid" :sch "Any"}
   :uri     {:dat "uri" :sch "Any"}
   :bytes   {:dat "byte" :sch "Any"}})

;; DATOMIC
(defn uniqueness-check [result uniq-attr]
  (if uniq-attr
    (assoc result :db/unique (keyword "db.unique" (lower-case uniq-attr)))
     result))

(defn datomic-schema-attribute
  "Build a datomic schema attribute. Required keys with string values- :ent :ident :type :cardinality (one or many)"
  [{:keys [ent ident type cardinality unique index doc fulltext is-component no-history]
    :or   {index true doc "No docs provided" fulltext false unique nil is-component false no-history false}}]
    (->
       {:db/id                (d/tempid :db.part/db)
       :db/ident              (keyword ent ident)
       :db/valueType          (keyword "db.type" (get-in data-types [(keyword (lower-case type)) :dat]))
       :db/cardinality        (keyword "db.cardinality" (lower-case cardinality))
       :db/doc                doc
       :db/index              index
       :db/fulltext           fulltext
       :db/isComponent        is-component
       :db/noHistory          no-history
       :db.install/_attribute :db.part/db}
     (uniqueness-check unique)))

(defn assoc-datomic-id
  "Returns map with transformed id key + temp id value for relational mapping on import"
  [{:keys [id] :as m}]
    (let [dat-id :db/id]
    (-> (rename-keys m {:id dat-id})
        (assoc dat-id (d/tempid :db.part/user id)))))

(defn build-datomic-schema [coll] (map datomic-schema-attribute coll))


;; SCHEMA
(defn filter-by-ns
  "Filters map based on given ent-ns value"
  [m ent-ns]
  (filter #(= ent-ns %) m))

(defn build-entity-attr
  "Takes a result map and a schema map to assoc required field or not"
  [m & [{:keys [ent ident type optional] :or [optional true]}]]
  (let [sch-optional #(s/optional-key %)]
    (let [sch-type (symbol sch-prefix (get-in data-types [(keyword (lower-case type)) :sch]))]
      (if optional
        (assoc m (sch-optional (keyword ident)) sch-type)
        (assoc m (keyword ident) sch-type)))))

(comment
(defn build-schema-lib
  [{:keys [ent ident type]}]
  (binding [sch-name (symbol sch-name)]
    (s/defschema sch-name
      {(keyword ident)(symbol sch-prefix (get-in data-types [(keyword (lower-case type)) :sch]))
       (s/optional-key :description) s/Str})
    (prn (class ent))
    ent))
)
;; SCHEMA END



(def datomic-schema (build-datomic-schema data-schema))
