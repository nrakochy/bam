(ns bam.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [bam.core-test]))

(doo-tests 'bam.core-test)

