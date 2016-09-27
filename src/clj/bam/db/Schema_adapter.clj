(ns bam.db.Schema-adapter
  (:require [clojure.string :refer [lower-case]]
            [bam.db.schema :as schema :refer [data-schema data-entities data-references data-types]]
            [schema.core :as s]))

;; dynamic symbols-> sch-prefix needs to match required schema.core :as value e.g. [schema.core :as s]
(def sch-prefix "s")
(def ^:dynamic sch-name (symbol "Schema"))

;; SCHEMA
(defn filter-by-ns
  "Filters map based on given ent-ns value"
  [m ent-ns]
  (filter #(= ent-ns %) m))

(defn build-entity-attr
  "Takes a result map and a schema map to assoc required field or not"
  [m & [{:keys [ent ident type optional] :or [optional true]}]]
  (let [sch-optional #(s/optional-key %)]
    (let [sch-type (symbol sch-prefix (get-in data-types [type :sch]))]
      (if optional
        (assoc m (sch-optional (keyword ident)) sch-type)
        (assoc m (keyword ident) sch-type)))))

(comment
  (defn build-schema-lib
    [{:keys [ent ident type]}]
    (binding [sch-name (symbol sch-name)]
      (s/defschema sch-name
        {(keyword ident)               (symbol sch-prefix (get-in data-types [(keyword (lower-case type)) :sch]))
         (s/optional-key :description) s/Str})
      (prn (class ent))
      ent))
  )
;; SCHEMA END
