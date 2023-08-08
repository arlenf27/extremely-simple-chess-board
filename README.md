# extremely-simple-chess-board
 - A preliminary and developmental version of a basic chess board with no GUI
 - Contains Java Class files, ***requires JDK to be installed***
 - To run the program, ***run ESCB1.class through a command prompt***, detailed instructions below
 - Takes input in the form of SAN (Standard Algebraic Notation) and makes the appropriate move on the board; prints the appropriate error message in the case of an invalid input
## Status
Unfinished: 
 - Not thoroughly tested yet, may contain errors
 - Draw by agreement not implemented
 - Draw by 50-move rule not implemented
 - Resignation not implemented
## Operation Notes
1) This program does not accept the letter 'P' as an initial for pawn moves; in these cases, the 'P' is ommitted
2) This program does not accept 'e.p' as part of an annotation; it will auto-recognize the validity of en passant moves
3) This program does not allow the presence of additional annotations on the quality of moves (such as '!' or '!!' or '?!')
4) The symbol '#' is used for checkmates instead of '++'
5) Unfortunately, I am not completely familiar with SAN; I have tried my best to make sure all possible inputs are accounted for, but I could have misunderstood a few things about the notation
6) If an invalid input was given, then the program will prompt for the input again
7) It is completely UNNECESSARY to include '+' or '#' as part of a move for checks or checkmates; however, if one chooses to include either character, then the move MUST be either a check or a checkmate
## Running the Program
1) Check that a recent version of JDK is installed (Recommended Minimum Version: JDK 17.0.7): `java -version`
2) Unzip the downloaded file
3) Navigate (`cd`) to the directory where the folder "escb1" and "README.md" are visible (**Do not go into the "escb1" directory**)
4) Run the ESCB1.class file through the escb1 directory: `java escb1.ESCB1`
