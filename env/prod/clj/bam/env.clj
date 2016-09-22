(ns bam.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[bam started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[bam has shut down successfully]=-"))
   :middleware identity})
