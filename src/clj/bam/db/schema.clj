(ns bam.db.schema)

(def data-schema
  ;; USER
  {:user
   [{:ident       "username"
     :type        :string
     :cardinality "one"
     :unique      "value"
     :doc         "Username"
     :optional    false}
    {:ident       "fullName"
     :type        :string
     :cardinality "one"
     :doc         "Name by which user wants to be known"
     :optional    false}
    {:ident       "email"
     :type        :string
     :unique      "value"
     :cardinality "many"
     :doc         "Email accounts of users"
     :optional    false}
    {:ent         "user"
     :ident       "bio"
     :type        :string
     :cardinality "one"
     :doc         "User bio"
     :optional    true}
    {:ident       "company"
     :type        :string
     :cardinality "one"
     :doc         "User Company"
     :optional    true}
    {:ident       "roles"
     :ref-ent     [:enum "role"]
     :type        :ref
     :cardinality "many"
     :doc         "Role in project"
     :optional    true}
    {:ident       "authentications"
     :ref-ent     [:authentication "name"]
     :type        :ref
     :cardinality "many"
     :doc         "Authentication services- probably OAuth"
     :optional    true}]
   ;; ORGANIZATIONS
   :org
   [{:ident       "name"
     :type        :string
     :cardinality "one"
     :doc         "Organization name"
     :optional    false}
    {:ident       "category"
     :ref-ent     [:category "name"]
     :type        :ref
     :cardinality "one"
     :doc         "Organization category"
     :optional    false}
    {:ident       "website"
     :type        :uri
     :cardinality "one"
     :doc         "Organization website"
     :optional    false}
    {:ident       "ein"
     :type        :string
     :cardinality "one"
     :doc         "Organization IRS employment id number"
     :optional    false}]
   ;; PROJECT
   :project
   [{:ident       "title"
     :type        :string
     :cardinality "one"
     :doc         "Project Name"
     :optional    false}
    {:ident       "description"
     :type        :string
     :cardinality "one"
     :doc         "Details of project"
     :optional    false}
    {:ident       "type"
     :type        :string
     :cardinality "one"
     :doc         "Type of project - on-going, feature development, start-up, etc."
     :optional    false}
    {:ident       "workScopes"
     :ref-ident   [:workScope "name"]
     :type        :ref
     :cardinality "many"
     :doc         "Details of project"
     :optional    false}]
   ;; WORK SCOPE
   :workScope
   [{:ident       "skills"
     :ref-ent     [:skill "name"]
     :type        :ref
     :cardinality "many"
     :doc         "Cf. of skills needed for the project - consultative, technological, data science-ing etc."
     :optional    true}
    {:ident       "description"
     :type        :string
     :cardinality "one"
     :doc         "Description of scope of work"
     :optional    false}
    {:ident       "name"
     :type        :string
     :cardinality "one"
     :doc         "Name Summary of workscope"
     :optional    false}
    {:ident       "budgetedHours"
     :type        :float
     :cardinality "one"
     :doc         "Requested time commitment"
     :optional    false}]
   ;; ENUMS (?)
   :enum
   [{:ident       "role"
     :type        :string
     :cardinality "one"
     :doc         "Role on the project - Developer, Owner, Requester, etc."
     :optional    false}]
   ;; Category
   :category
   [{:ident       "name"
     :type        :string
     :cardinality "one"
     :doc         "Org Categorization - Community Dev, Education, etc."
     :optional    false}
    {:ident       "cause"
     :type        :string
     :cardinality "one"
     :doc         "Sub-category of organization - Education -> early childhood, scholarship, youth ed, etc. "
     :optional    false}]
   ;; SKILLS
   :skill
   [{:ident       "name"
     :type        :string
     :cardinality "one"
     :doc         "Skill Name - Clojure, Postgresql, Ruby, Wordpress, etc."
     :optional    true}
    {:ident       "type"
     :type        :string
     :cardinality "one"
     :doc         "Type of skill - technology, project management, design, etc."
     :optional    false}]
   ;; AUTHENTICATION
   :authentication
   [{:ident       "type"
     :type        :string
     :cardinality "one"
     :doc         "Type of auth service"
     :optional    false}
    {:ident       "name"
     :type        :string
     :cardinality "one"
     :doc         "Name of auth service - Githubs, Linkedin, Facebooks, etc."
     :optional    false}
    {:ident       "token"
     :type        :string
     :cardinality "one"
     :doc         "Logging it in with a token"
     :optional    false}]})

(def data-entities (keys data-schema))

(defn ent-idents [coll]
  (mapv #(:ident %) coll))

(defn ident-by-ent-key
  "Retrieve all idents of data-schema as a vector of strings of a given entity key.
  i.e. passing in :org might return ['name' 'category' 'website' 'ein']"
  [ent-key]
  (ent-idents (ent-key data-schema)))

(defn get-reference-idents
  "Retrieves all idents with type = ref from data-schema as vector of strings."
  [coll]
  (->> (filterv #(= :ref (:type %)) coll)
       (ent-idents)))

(def data-references
  "Returns hash of entity as key and vector of stringified ref idents as value"
  (->> (mapv #(get-reference-idents (get data-schema %)) data-entities)
       (zipmap data-entities)))

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
