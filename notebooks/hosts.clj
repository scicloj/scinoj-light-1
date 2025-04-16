;; # Hosts

^{:clay {:hide-code true}}
(ns hosts
  (:require [utils]
            [scicloj.kindly.v4.kind :as kind]))

(-> (utils/info)
    :hosts
    utils/people-md
    kind/md)

