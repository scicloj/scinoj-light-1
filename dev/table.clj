(ns table
  (:require [tablecloth.api :as tc]
            [utils]))

(-> (utils/info)
    :schedule-drafts
    vals
    last
    tc/dataset
    (tc/write-csv! "/tmp/schedule.csv"))
