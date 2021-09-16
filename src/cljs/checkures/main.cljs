(ns ^:figwheel-hooks checkures.main
  (:require
   [checkures.utils :as utils]
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

;; TODO: implement win condition
;;       timer
;;       key listener
;;       reset game
;;       undo move
;;       remove fig-wheel defaults
;;       2 player
;;       server
;;       piece counter

(defonce app-state (atom {:turn :white
                          :board utils/init-pos
                          :selected []}))

(defn make-id
  [col row]
  (str col "-" row))

(defn set-class
  [[col row] cl]
  (let [el (gdom/getElement (make-id col row))]
    (gdom/setProperties el (js-obj "class" cl))))

(defn make-selection
  [target]
  (let [sel (:selected @app-state)]
    (swap! app-state assoc :selected (conj sel target))
    (set-class target "selected")))

(defn undo-selection
  []
  (let [sel (:selected @app-state)]
    (when (> (count sel) 0)
      (set-class (peek sel) "unselected")
      (swap! app-state assoc :selected (pop sel)))))

(defn clear-selections
  []
  (let [sel (:selected @app-state)]
    (run! #(set-class % "unselected") sel)
    (swap! app-state assoc :selected [])))

(defn exec-move
  []
  (let [board (@app-state :board)
        selected (@app-state :selected)
        turn (@app-state :turn)]
    (if (utils/valid-turn? board selected turn)
      (do
        (println "turn is valid")
        (swap! app-state assoc
               :board (utils/king-me
                       (utils/make-moves board selected)))
        (swap! app-state assoc :turn (if (= turn :red) :white :red))
        (clear-selections))
      (do
        (js/alert (str "Invalid turn " selected))
        (clear-selections)))))

;; TODO: *-row functions need to be refactored, this is a mess...
(defn even-row
  "turn even rows into html"
  [i row]
  (let [index (* i 2)]
    (into [:tr {:row index}]
          (interleave (repeat 4 [:td {:class :unplayable}])
                      (map-indexed (fn [col cl]
                                     vec [:td {:id (make-id col index)
                                               :class "unselected"
                                               :col col
                                               :row index
                                               :on-click #(make-selection [col index])}
                                          [:div {:class cl}
                                           (when (or (= cl :red-king)
                                                     (= cl :white-king))
                                             [:p "♛"])]])
                                   row)))))

(defn odd-row
  "turn odd rows into html"
  [i row]
  (let [index (inc (* i 2))]
    (into [:tr {:row index}]
          (interleave (map-indexed (fn [col cl]
                                     vec [:td {:id (make-id col index)
                                               :class "unselected"
                                               :col col
                                               :row index
                                               :on-click #(make-selection [col index])}
                                          [:div {:class cl}
                                           (when (or (= cl :red-king)
                                                     (= cl :white-king))
                                             [:p "♛"])]])
                                   row)
                      (repeat 4 [:td {:class :unplayable}])))))

(defn show-board
  [board]
  (into [:table]
        (interleave
         (map-indexed even-row (take-nth 2 board))
         (map-indexed odd-row (take-nth 2 (rest board))))))

(defn get-app-element []
  (gdom/getElement "app"))

(defn hello-world []
  [:div
   [:h1 "Checkures"]
   [:p (str "It is " (if (= (:turn @app-state) :white) "white's" "red's") " turn!")]
   [:button {:on-click #(undo-selection)} "undo"]
   [:button {:on-click #(clear-selections)} "clear"]
   [:button {:on-click #(exec-move)} "execute move"]
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
