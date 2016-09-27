(ns bam.io.file-io
  (:require [clojure.java.io :as fio :refer [reader file]]
            [clojure.string :as str :refer [split]]
            [clojure.walk :refer [keywordize-keys]]))

(defn file-dir
  "Returns canoncial path of a given path"
  [path]
  (.getCanonicalPath (fio/file path)))

(defn split-commas [line]
  (str/split (str line) #","))

(defn set-kv [coll]
  (-> (map #(zipmap (first coll) %) (rest coll))
      (keywordize-keys)))

(defn parse-csv
  "Converts CSV- returns seq of maps, first line as key in each map, every line thereafter as values"
  [read-file]
  (with-open [r (reader (file-dir read-file))]
    (let [all-lines (line-seq r)]
      (doall (set-kv (map #(split-commas %) all-lines))))))
