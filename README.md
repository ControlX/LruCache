# LruCache
A utility class for caching bitmaps/images, generally used in lazy loading. This class can be forked and made generic according to user requirements. A context reference is passed in class by which a user can enable caching in file system rather than in objects. This class follows queue structure internally wherein eldest entries are removed first if the max size of entries are achieved.
