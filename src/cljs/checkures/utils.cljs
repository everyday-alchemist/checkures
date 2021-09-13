(ns checkures.utils)

(def init-pos
  [[:black :black :black :black]
   [:black :black :black :black]
   [:black :black :black :black]
   [:none :none :none :none]
   [:none :none :none :none]
   [:red :red :red :red]
   [:red :red :red :red]
   [:red :red :red :red]])

(def edges
  #{#{[0 0] [0 1]}
    #{[0 0] [1 1]}
    #{[1 0] [1 1]}
    #{[1 0] [2 1]}
    #{[2 0] [2 1]}
    #{[2 0] [3 1]}
    #{[3 0] [3 1]}

    #{[0 1] [0 2]}
    #{[1 1] [0 2]}
    #{[1 1] [1 2]}
    #{[2 1] [1 2]}
    #{[2 1] [2 2]}
    #{[3 1] [2 2]}
    #{[3 1] [3 2]}

    #{[0 2] [0 3]}
    #{[0 2] [1 3]}
    #{[1 2] [1 3]}
    #{[1 2] [2 3]}
    #{[2 2] [2 3]}
    #{[2 2] [3 3]}
    #{[3 2] [3 3]}

    #{[0 3] [0 4]}
    #{[1 3] [0 4]}
    #{[1 3] [1 4]}
    #{[2 3] [1 4]}
    #{[2 3] [2 4]}
    #{[3 3] [2 4]}
    #{[3 3] [3 4]}

    #{[0 4] [0 5]}
    #{[0 4] [1 5]}
    #{[1 4] [1 5]}
    #{[1 4] [2 5]}
    #{[2 4] [2 5]}
    #{[2 4] [3 5]}
    #{[3 4] [3 5]}

    #{[0 5] [0 6]}
    #{[1 5] [0 6]}
    #{[1 5] [1 6]}
    #{[2 5] [1 6]}
    #{[2 5] [2 6]}
    #{[3 5] [2 6]}
    #{[3 5] [3 6]}

    #{[0 6] [0 7]}
    #{[0 6] [1 7]}
    #{[1 6] [1 7]}
    #{[1 6] [2 7]}
    #{[2 6] [2 7]}
    #{[2 6] [3 7]}
    #{[3 6] [3 7]}})

(defn get-2d
  [board col row]
  (get (get board row) col))

(defn set-2d
  [board col row v]
  (let [old-row (get board row)]
    (assoc board row (assoc old-row col v))))

(defn move 
  [board [from-col from-row] [to-col to-row]]
  (let  [piece (get-2d board from-col from-row)]
    (-> board 
        (set-2d from-col from-row :none)
        (set-2d to-col to-row piece))))

(defn conn?
  "returns true if from is connected to to"
  [adj from to]
  (contains? adj #{from to}))

(defn occupied?
  [board col row]
  (not= (get-2d board col row) :none))

(defn get-dir
  ; TODO refactor to only take col?
  "returns the direction of a move from arg1 to arg2"
  [[_ y1] [_ y2]]
  (if (< y1 y2)
    :down
    :up))

(defn get-valid-dirs
  [piece]
  (case piece
    :red #{:up}
    :black #{:down}
    #{:up :down}))

(defn valid-move?
  [board [from-col from-row] [to-col to-row]]
  (let [from-piece (get-2d board from-col from-row)
        to-piece (get-2d board to-col to-row)
        dir (get-dir [from-col from-row] [to-col to-row])
        valid-dirs (get-valid-dirs from-piece)
        dist (- from-row to-row)]
    (println (str from-piece " " to-piece " " dir " " valid-dirs " " dist))
    (and
     (not= from-piece :none)
     (= to-piece :none)
     (conn? edges [from-col from-row] [to-col to-row]) ; TODO: keep as constant?
     (contains? valid-dirs dir)
     (or (= dist 1) (= dist -1)))))
