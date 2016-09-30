(ns bam.db.repo-service
  (:require [bam.db.core :as db :refer [retrieve-entity]]))

(defn get [m]
  (db/retrieve-entity m))
