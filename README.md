# Theo Lee - Unit 7 Project

# Mr. Keithley, before running my program, please read the following:

## This program is intended to work like an SQL database with UI.
## Most of the menu features have directions, but the "query" feature is a little complex.

## The "query" feature works like SQL where you type a command to get a view over the dataset:
## ex. Query all the people that have a name length of 4 letters and a GPA above 3.0 (pseudocode)

## In my program, queries done similarly:
## (Actual code):
## Query for: BRAND EQ Kellogs
## ...PRICE GE 100.50
## ...STOCK LT 30
## ...EXIT

## In the code above, it says:
## "Find all groceries with a brand called Kellogs, 
## a price greater than or equal to $100.50,
## and this item must have less than 30 of its kind in stock."
## The items will be printed after "EXIT" is entered.

## Here is a translation of all the keywords that can be used in the query:
## ID -> find all items with an ID that is...
## BRAND -> find all items with a brand that is...
## NAME -> find all items with a name that is...
## PRICE -> find all items with a price that is...
## STOCK -> find all items with a stock that is...
## LT -> less than
## LE -> less than or equal to
## GT -> greater than
## GE -> greater than or equal to
## EQ -> equal to
## NE -> not equal to
## EXIT -> exit query chain

## In addition to the query feature, there is also a "dynamic" sorter
## Instead of having static methods e.g. "sortByName(), sortByPrice()", etc,

## You can just use the sort method as follows:
## sort(groceries, store, "ASCENDING", "BRAND");
## ASCENDING means smallest to largest/a-z/1-100/etc.
## DESCENDING means largest to smallest/z-a/100-1/etc.

## The 4th argument is the "filter" of the sort
## "ID" means sort by ID numerically
## "BRAND" means sort by brand alphabetically
## "NAME" means sort by name alphabetically
## "PRICE" means sort by price numerically
## "STOCK" means sort by item count numerically

## The DESCENDING and ASCENDING keywords are the way to reverse and de-reverse
## the groceries listing order.

## Most of this is abstracted away, but please be careful to ensure that keywords
## are both spelled correctly and in the correct case because they are case-sensitive
