(ns bam.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [bam.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[bam started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[bam has shut down successfully]=-"))
   :middleware wrap-dev})
