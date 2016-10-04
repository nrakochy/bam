(ns bam.db.repo-service
  (:require [bam.db.core :as db :refer [retrieve-with]]))

(defn retrieve [m]
  (db/retrieve-with m))
