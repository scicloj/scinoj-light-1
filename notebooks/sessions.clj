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
       (mapcat
        (fn [[session-type type-sessions]]
          (cons
           (kind/md
            (format "## %s\n"
                    (case session-type
                      :special "Special"
                      :data-analysis "Data analysis stories"
                      :background "Background knowledge")))
           (->> type-sessions
                (mapcat
                 (fn [[title {:keys [session-type speakers abstract youtube-id]}]]
                   [(kind/md
                     (format "### %s\n%s\n\n%s\n"
                             title
                             abstract
                             (->> speakers
                                  sort
                                  (utils/people-md {:include-bio false
                                                    :depth 0
                                                    :link true
                                                    :image-height 100}))))
                    (if youtube-id
                      (kind/video {:youtube-id youtube-id})
                      (kind/hidden nil))]))))))
       kind/fragment))



