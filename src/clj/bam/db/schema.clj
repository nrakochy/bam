(ns bam.db.schema)

(defn get-reference-idents [coll]
  (->> (filterv #(= :ref (:type %)) coll)
       (mapv #(:ident %))))

(defn extract-references [entities]
  (->> (mapv #(get-reference-idents (get data-schema %)) entities)
       (zipmap data-entities)))

(def data-schema
  ;;:optional flag is for Schema not Datomic
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
     :type        :ref
     :cardinality "many"
     :doc         "Role in project"
     :optional    true}
    {:ident       "authentications"
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
     :type        :ref
     :cardinality "many"
     :doc         "Details of project"
     :optional    false}]
   ;; WORK SCOPE
   :workScope
   [{:ident       "skills"
     :type        :ref
     :cardinality "many"
     :doc         "Cf. of skills needed for the project - consultative, technological, data science-ing etc."
     :optional    true}
    {:ident       "description"
     :type        :string
     :cardinality "one"
     :doc         "Description of scope of work"
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
    {:ent         "authentication"
     :ident       "token"
     :type        :string
     :cardinality "one"
     :doc         "Logging it in with a token"
     :optional    false}]})

(def data-entities (keys data-schema))

(def data-references
   (extract-references data-entities))

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
