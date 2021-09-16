(ns ^:figwheel-hooks checkures.main
  (:require
   [checkures.utils :as utils]
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]))

;; TODO: implement buttons to execute/clear move
;;       add movement indicators
;;         [ ] unselect all squares after trying to move
;;       allow multi-jump moves
;;       implement turns and win condition
;;       general styling improvements

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:board utils/init-pos
                          :selected nil}))

(defn make-id
  [col row]
  (str col "-" row))

;; TODO: this will need to account for whose turn it is once implemented
(defn handle-click
  [target]
  (let [selected (get @app-state :selected)
        board (get @app-state :board)
        el (gdom/getElement (make-id (target 0) (target 1)))]
    (if selected
      (if (utils/valid-move? board selected target)
        (swap! app-state assoc
               :board (utils/king-me
                       (utils/move board selected target))
               :selected nil)
        (do (js/alert (str "Invalid move " selected ":" target))
            (swap! app-state dissoc :selected)))
      (do (swap! app-state assoc :selected target)
          (gdom/setProperties el (js-obj "class" "selected"))))))

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
                                               :on-click #(handle-click [col index])}
                                          [:div {:class cl}
                                           (when (or (= cl :red-king)
                                                     (= cl :black-king))
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
                                               :on-click #(handle-click [col index])}
                                          [:div {:class cl}
                                           (when (or (= cl :red-king)
                                                     (= cl :black-king))
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
