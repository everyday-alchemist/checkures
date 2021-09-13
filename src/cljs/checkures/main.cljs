(ns ^:figwheel-hooks checkures.main
  (:require
   [checkures.utils :as utils]
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

(defn even-row
  "turn even rows into html"
  [i row]
  (let [index (* i 2)]
    (into [:tr {:row index}]
          (interleave (repeat 4 [:td {:class :unplayable}])
                      (map-indexed #(vec [:td (assoc {} :class %2 :row index :col %1)]) row)))))

(defn odd-row
  "turn odd rows into html"
  [i row]
  (let [index (inc (* i 2))]
    (into [:tr {:row index}]
          (interleave (map-indexed #(vec [:td (assoc {} :class %2 :row index :col %1)]) row)
                      (repeat 4 [:td {:class :unplayable}])))))

(defn show-board
  [board]
  (into [:table]
        (interleave
         (map-indexed even-row (take-nth 2 board))
         (map-indexed odd-row (take-nth 2 (rest board))))))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"
                          :board utils/init-pos}))

(defn get-app-element []
  (gdom/getElement "app"))

(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:h3 "Rock rock on!!!"]
   (show-board (:board @app-state))])

(defn mount [el]
  (rdom/render [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
