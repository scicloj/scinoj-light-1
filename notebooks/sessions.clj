;; # Sessions

^{:clay {:hide-code true}}
(ns sessions
  (:require [scicloj.kindly.v4.kind :as kind]
            [utils]
            [clojure.string :as str]))

(let [{:keys [sessions people]} (utils/info)]
  (->> sessions
       (group-by (fn [[_ {:keys [session-type]}]]
                   session-type))
       (map (fn [[session-type type-sessions]]
              (str (format "## %s\n"
                           (case session-type
                             :special "Special"
                             :data-analysis "Data analysis stories"
                             :background "Background knowledge"))
                   (->> type-sessions
                        (map (fn [[title {:keys [session-type speakers abstract]}]]
                               (format "### %s\n%s\n%s\n"
                                       title
                                       (->> speakers
                                            sort
                                            (utils/people-md {:include-bio false
                                                              :depth 0
                                                              :link true
                                                              :image-height 100}))
                                       abstract)))
                        (str/join "\n")))))
       (str/join "\n")
       kind/md))



