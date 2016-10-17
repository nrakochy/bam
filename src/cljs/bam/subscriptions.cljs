(ns bam.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :docs
  (fn [db _]
    (:docs db)))

(reg-sub
  :sidebar-collapsed?
  (fn [db _]
    (:sidebar-collapsed? db)))

(reg-sub
  :sidebar-state
  (fn [db _]
    (if (:sidebar-collapsed? db)
        "row-offcanvas-left"
        "row-offcanvas-left active")))
