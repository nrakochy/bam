(ns user
  (:require [mount.core :as mount]
            [bam.figwheel :refer [start-fw stop-fw cljs]]
            bam.core))

(defn start []
  (mount/start-without #'bam.core/repl-server))

(defn stop []
  (mount/stop-except #'bam.core/repl-server))

(defn restart []
  (stop)
  (start))


