;; # Schedule Drafts

;; The content of this page is temporary and is used in the planning stage.

;; - All times are in [UTC](https://time.is/UTC) time zone.
;; - All talk durations include the discussion/Q&A part.
;; - Data analysis talks are all 30 minute talk (some pre-recorded) + 20 minute discussion.
;; - Background talks and special sessions are often longer.
;; - A few of the background talks (intro to Noj, intro to probability) will be similar to the content of [the prep workshop](https://scicloj.github.io/blog/data-analyis-with-clojure-free-workshop-may-10th-initial-survey/), so people who miss the first part of the day will not miss too much.
;; - The first day's full recording will be published almost immediately, so people can review the parts they've missed before the second day.


^{:clay {:hide-code true}}
(ns schedule
  (:require [scicloj.kindly.v4.kind :as kind]
            [utils]
            [clojure.string :as str]
            [tablecloth.api :as tc]))

(def *counter (atom 0))
(defn next-id! []
  (str "id-" (swap! *counter inc)))

(defn collapsible [{:keys [title content callout-type]}]
  (let [id (next-id!)]
    (kind/hiccup
     [:div
      [:div
       {:class (str
                "callout callout-style-default "
                "callout-" (name callout-type))}
       [:div.callout-header
        {:data-bs-toggle :collapse
         :data-bs-target (str "." id "-contents")}
        [:div.callout-title-container
         [:p.big (str title " >")]]]
       [:div.callout-collapse.collapse {:id id
                                        :class (str id "-contents")}
        [:div.callout-body-container.callout-body
         content]]]])))

(let [{:keys [schedule-drafts sessions people]} (utils/info)]
  (kind/fragment
   (concat [(kind/md "::: {.panel-tabset}")]
           (->> schedule-drafts
                (mapcat
                 (fn [[draft-name schedule]]
                   (let [day->schedule (-> schedule
                                           tc/dataset
                                           (tc/group-by :day {:result-type
                                                              :as-map}))]
                     [(kind/md (format "## %s\n" draft-name))
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
                                   (tc/map-columns :split-time [:time] #(str/split % #"-"))
                                   (tc/map-columns :start [:split-time] first)
                                   (tc/map-columns :end [:split-time] second)
                                   (tc/map-columns
                                    :title
                                    [:title]
                                    (fn [title]
                                      (let [{:keys [type
                                                    speakers
                                                    abstract]} (sessions title)
                                            content [:div
                                                     [:i (some->>
                                                          type
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
                                                     (kind/md abstract)]]
                                        (kind/hiccup
                                         [:div {:background-color "#550055"}
                                          (collapsible
                                           {:title title
                                            :content content
                                            :callout-type (case type
                                                            :special :important
                                                            :data-analysis :note
                                                            :background :tip
                                                            ;; else
                                                            :caution)})]))))
                                   (tc/select-columns [:time :title])
                                   (kind/table {:use-datatables true
                                                :datatables {:info false
                                                             :scrollY false
                                                             :columns [{:width "15%"}
                                                                       {:width "85%"}]}}))]))
                           kind/fragment)
                      (kind/md ":::")]))))
           [(kind/md ":::")])))

