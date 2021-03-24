# item-trader
Post offers and bids that get matched automatically.
Item names must be unique.
Each item has:
  - A priority queue for bids - highest price gets highest priority
  - A priority queue for offers - lowest price gets highest priority
When the highest priority bid has a price greater than or equal to
the price of the highest priority offer, the two get matched and are
deleted from the item's priority queues.
