;; # Speakers

^{:clay {:hide-code true}}
(ns speakers
  (:require [utils]
            [scicloj.kindly.v4.kind :as kind]))

(-> (utils/info)
    :speakers
    utils/people-md
    kind/md)


