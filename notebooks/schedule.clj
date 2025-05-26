;; # Schedule

;; Here is the schedule we had for the two conference days.
;; All times are in [UTC](https://time.is/UTC) time zone.


^{:clay {:hide-code true}}
(ns schedule
  (:require [scicloj.kindly.v4.kind :as kind]
            [utils]
            [clojure.string :as str]
            [tablecloth.api :as tc]))

(let [{:keys [schedule-drafts sessions people]} (utils/info)]
  (kind/fragment
   (->> schedule-drafts
        (mapcat
         (fn [[draft-name schedule]]
           (let [day->schedule (-> schedule
                                   tc/dataset
                                   (tc/group-by :day {:result-type
                                                      :as-map}))]
             [#_(kind/md (format "(%s)\n" draft-name))
              ;; (kind/md "::: {.panel-tabset}")
              (->> [:Fri :Sat]
                   (mapcat
                    (fn [day]
                      [(kind/md
                        (case day
                          :Fri "### Fri May 16th"
                          :Sat "### Sat May 17th\n(recordings coming soon)"))
                       (-> day
                           day->schedule
                           (tc/map-columns
                            :title
                            [:title]
                            (fn [title]
                              (let [{:keys [session-type
                                            speakers
                                            abstract
                                            youtube-id]} (sessions title)]
                                (kind/hiccup
                                 [:div
                                  (if abstract
                                    [:div.rounded {:style {:background-color
                                                           (case session-type
                                                             :special "#ffeeff"
                                                             :background "#ffffee"
                                                             :data-analysis "#eeffff"
                                                             "#ffffff")}}
                                     [:details {:style {:margin "20px"}}
                                      [:summary title]
                                      [:div
                                       [:i (some->> session-type
                                                    name
                                                    (format "(%s session)"))]
                                       (->> speakers
                                            sort
                                            (utils/people-hiccup
                                             {:include-bio false
                                              :depth 0
                                              :link true
                                              :image-height 100})
                                            kind/hiccup)
                                       [:br]
                                       (kind/md abstract)]]
                                     (kind/video {:youtube-id youtube-id})]
                                    title)]))))
                           (tc/select-columns [:time :title])
                           tc/rows
                           (kind/table {:style {:table-layout :auto}}))]))
                   kind/fragment)
              ;; (kind/md ":::")
              ]))))))

