;; # Tentative Schedule

;; The content of this page is temporary and is used in the planning stage.

;; - All times are in [UTC](https://time.is/UTC) time zone.
;; - All talk durations include the discussion/Q&A part.
;; - Data analysis talks are all 30 minute talk (some pre-recorded) + 20 minute discussion.
;; - Background talks and special sessions are often longer.
;; - A few of the background talks (intro to Noj, intro to probability) will mostly repeat the content of [the prep workshop](https://scicloj.github.io/blog/data-analyis-with-clojure-free-workshop-may-10th-initial-survey/), so people who miss the first part of the day will not miss too much.
;; - The first day's full recording will be published at the end of that day, so people can review the parts they've missed before the second day.


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
             [(kind/md (format "(%s)\n" draft-name))
              (kind/md "::: {.panel-tabset}")
              (->> [:Fri :Sat]
                   (mapcat
                    (fn [day]
                      [(kind/md
                        (case day
                          :Fri "### Fri May 16th"
                          :Sat "### Sat May 17th"))
                       (-> day
                           day->schedule
                           (tc/map-columns
                            :title
                            [:title]
                            (fn [title]
                              (let [{:keys [session-type
                                            speakers
                                            abstract]} (sessions title)]
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
                                       (kind/md abstract)]]]
                                    title)]))))
                           (tc/select-columns [:time :title])
                           tc/rows
                           (kind/table {:style {:table-layout :auto}}))]))
                   kind/fragment)
              (kind/md ":::")]))))))

