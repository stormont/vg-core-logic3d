
module Main
   where

import qualified Data.ByteString.Lazy.Char8 as BL8
import qualified Data.Map.Lazy              as Map
import Data.List  (find)
import Data.Maybe (fromJust, isJust)


data GameConfig = GameConfig
                { gameConfigSize    :: Integer
                , gameConfigPlayers :: Integer
                } deriving (Show,Read)


type Index = (Integer,Integer,Integer)


type Player = Integer


data PieceIndex = PieceIndex
                { pieceIndex  :: Index
                , piecePlayer :: Maybe Player
                } deriving (Show,Read,Eq)


type StringLookup = Map.Map ActionResult BL8.ByteString


data ActionResult = GameSizeBelowMinimum
                  | GamePlayersBelowMinimum
                  | InvalidPieceIndex
                  | InvalidPlayerSpecified
                  | NotPlayerTurn
                  | GameAlreadyCompleted
                  deriving (Show,Read,Eq,Ord)


type Solution = [PieceIndex]


data GameCondition = GameInPlay
                   | GameCompleted
                   deriving (Show,Read,Eq,Ord)


data Game = Game
          { gameConfig    :: GameConfig
          , gameStrings   :: StringLookup
          , gamePieces    :: [PieceIndex]
          , gameTurn      :: Player
          , gameCondition :: GameCondition
          , gameSolution  :: Maybe (Solution, Player)
          } deriving (Show,Read)


type ErrorResult = BL8.ByteString


g_solutionSize   = 4
g_minGamePlayers = 2


g_enStringMap = Map.fromList [ (GameSizeBelowMinimum, BL8.pack "Game size is below minimum")
                             , (GamePlayersBelowMinimum, BL8.pack "Number of game players is below minimum")
                             , (InvalidPieceIndex, BL8.pack "Game piece cannot be placed here")
                             , (InvalidPlayerSpecified, BL8.pack "Game doesn't have this many players")
                             , (NotPlayerTurn, BL8.pack "It is not this player's turn")
                             , (GameAlreadyCompleted, BL8.pack "Game has already been completed")
                             ]


-------------------------------------------------------------------------------
-- PUBLIC FUNCTIONS
-------------------------------------------------------------------------------


initializeGame :: StringLookup -> GameConfig -> Either ErrorResult Game
initializeGame strIndex gc =
   case validateGameConfig strIndex gc of
      Left e -> Left e
      _      -> Right game
   where pieces = initializePieceIndices $ gameConfigSize gc
         game = Game gc strIndex pieces 1 GameInPlay Nothing


setGamePiece :: Game -> PieceIndex -> Either ErrorResult Game
setGamePiece game index =
   if gameCondition game == GameCompleted
      then Left $ lookupString (gameStrings game) GameAlreadyCompleted
      else
         case piecePlayer index of
            Nothing -> error $ "No player has been specified for this move"
            Just p  ->
               if p > gamePlayers
                  then Left $ lookupString (gameStrings game) InvalidPlayerSpecified
                  else
                     if p /= gameTurn game
                        then Left $ lookupString (gameStrings game) NotPlayerTurn
                        else
                           case (piecePlayer $ head matching) of
                              Just _ -> Left $ lookupString (gameStrings game) InvalidPieceIndex
                              _      ->
                                 case solved of
                                    Nothing -> Right $ game' { gameTurn = nextPlayer }
                                       where nextPlayer =
                                                if p == gamePlayers
                                                   then 1
                                                   else p + 1
                                    Just _  -> Right $ game'
                              where pi = pieceIndex index
                                    matchFilter x = pieceIndex x == pi
                                    notMatching = filter (not . matchFilter) $ gamePieces game
                                    matching = filter matchFilter $ gamePieces game
                                    (game', solved) = checkGameSolution $ game { gamePieces = (index : notMatching) }
   where gamePlayers = gameConfigPlayers $ gameConfig game


checkGameSolution :: Game -> (Game, Maybe (Solution, Player))
checkGameSolution game =
   if gameCondition game == GameCompleted
      then (game, gameSolution game)
      else
         case checkSolution $ gamePieces game of
            Nothing -> (game, Nothing)
            Just s  -> (game { gameCondition = GameCompleted, gameSolution = details }, details)
               where details = Just (s, gameTurn game)


-------------------------------------------------------------------------------
-- INTERNAL
-------------------------------------------------------------------------------


validateGameConfig :: StringLookup -> GameConfig -> Either ErrorResult GameConfig
validateGameConfig strIndex gc
   | (gameConfigSize gc) < g_solutionSize      = Left $ lookupString strIndex GameSizeBelowMinimum
   | (gameConfigPlayers gc) < g_minGamePlayers = Left $ lookupString strIndex GamePlayersBelowMinimum
   | otherwise = Right gc


initializePieceIndices :: Integer -> [PieceIndex]
initializePieceIndices size =
   map (\x -> PieceIndex x Nothing) [ (i,j,k) | i <- [0 .. (size - 1)], j <- [0 .. (size - 1)], k <- [0 .. (size - 1)] ]


lookupString :: StringLookup -> ActionResult -> BL8.ByteString
lookupString strIndex def =
   case Map.lookup def strIndex of
      Nothing -> error $ (show def) ++ " is not a defined string"
      Just x  -> x


checkSolution :: [PieceIndex] -> Maybe Solution
checkSolution pieces =
   if solved == []
      then Nothing
      else head solved
   where solutions = map (checkSolutionForIndex pieces) pieces
         solved = filter isJust solutions


checkSolutionForIndex :: [PieceIndex] -> PieceIndex -> Maybe Solution
checkSolutionForIndex _      (PieceIndex _     Nothing) = Nothing
checkSolutionForIndex pieces (PieceIndex index player)   =
   if solution == []
      then Nothing
      else Just $ head solution
   where deltaFunc (i,j,k) (x,y,z) = (i + x, j + y, k + z)
         funcs = map deltaFunc
               $ filter (/= (0,0,0))
               $ [ (i,j,k) | i <- [-1 .. 1], j <- [-1 .. 1], k <- [-1 .. 1]]
         indexLists = filter (\x -> (toInteger $ length x) >= g_solutionSize)
                    $ map (selectIndices index) funcs
         findPlayer x = find (\y -> pieceIndex y == x) pieces
         mappedPieces = map (map (fromJust . findPlayer)) indexLists
         solution = filter (all (\x -> piecePlayer x == player)) mappedPieces


selectIndices :: Index -> (Index -> Index) -> [Index]
selectIndices index func = selectIndices' [index] 1 index func
   where selectIndices' acc count index func
            | count >= g_solutionSize      = acc
            | i < 0 || i >= g_solutionSize = acc
            | j < 0 || j >= g_solutionSize = acc
            | k < 0 || k >= g_solutionSize = acc
            | otherwise                    = selectIndices' (cur : acc) (count + 1) cur func
            where cur@(i,j,k) = func index
