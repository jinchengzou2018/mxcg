package com.mxcg.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 
 * 排序方式
 * 
 *
 */
public class PageSort implements Iterable<PageSort.Order>, Serializable
{
    
    private static final long serialVersionUID = 5737186511678863905L;
    
    private static final PageSort UNSORTED = PageSort.by(new Order[0]);
    
    public static final Direction DEFAULT_DIRECTION = Direction.ASC;
    
    private List<Order> orders;
    
    public PageSort()
    {
    }
    
    private PageSort(Order... orders)
    {
        this(Arrays.asList(orders));
    }
    
    private PageSort(List<Order> orders)
    {
        
        Assert.notNull(orders, "Orders must not be null!");
        
        this.orders = Collections.unmodifiableList(orders);
    }
    
    private PageSort(String... properties)
    {
        this(DEFAULT_DIRECTION, properties);
    }
    
    /**
     * 创建一个排序 {@link Sort}.
     * 
     * @param direction 默认为 {@link Sort#DEFAULT_DIRECTION} (for {@literal null} cases, too)
     * @param properties 不可为 {@literal null}, 空串.
     */
    public PageSort(Direction direction, String... properties)
    {
        this(direction, properties == null ? new ArrayList<>() : Arrays.asList(properties));
    }
    
    /**
     * 创建一个排序 {@link Sort}.
     * 
     * @param direction 默认为 {@link Sort#DEFAULT_DIRECTION} (for {@literal null} cases, too)
     * @param properties 不可为 {@literal null}, 空串.
     */
    public PageSort(Direction direction, List<String> properties)
    {
        
        if (properties == null || properties.isEmpty())
        {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }
        
        this.orders = new ArrayList<>(properties.size());
        
        for (String property : properties)
        {
            this.orders.add(new Order(direction, property));
        }
    }
    
    public PageSort(Sort sort)
    {
        this.orders = new ArrayList<Order>();
        Iterator<Sort.Order> iterator = sort.iterator();
        while (iterator.hasNext())
        {
            Sort.Order o = iterator.next();
            this.orders.add(new Order(o));
        }
    }
    
    /**
     * 创建一个排序 {@link Sort}.
     * 
     * @param properties 不可为 {@literal null}, 空串.
     * @return
     */
    public static PageSort by(String... properties)
    {
        
        Assert.notNull(properties, "Properties must not be null!");
        
        return properties.length == 0 ? PageSort.unsorted() : new PageSort(properties);
    }
    
    /**
     * 创建一个排序 {@link Sort}.
     * 
     * @param orders 不可为 {@literal null}.
     * @return
     */
    public static PageSort by(List<Order> orders)
    {
        
        Assert.notNull(orders, "Orders must not be null!");
        
        return orders.isEmpty() ? PageSort.unsorted() : new PageSort(orders);
    }
    
    /**
     * 创建一个排序 {@link Sort}.
     * 
     * @param orders 不可为 {@literal null}.
     * @return
     */
    public static PageSort by(Order... orders)
    {
        
        Assert.notNull(orders, "Orders must not be null!");
        
        return new PageSort(orders);
    }
    
    /**
     * 创建一个排序 {@link Sort}.
     * 
     * @param direction 不可为 {@literal null}
     * @param properties 不可为 {@literal null}, 空串.
     * @return
     */
    public static PageSort by(Direction direction, String... properties)
    {
        
        Assert.notNull(direction, "Direction must not be null!");
        Assert.notNull(properties, "Properties must not be null!");
        Assert.isTrue(properties.length > 0, "At least one property must be given!");
        
        return PageSort.by(Arrays.stream(properties)//
            .map(it -> new Order(direction, it))//
            .collect(Collectors.toList()));
    }
    
    /**
     * Returns a {@link Sort} instances representing no sorting setup at all.
     * 
     * @return
     */
    public static PageSort unsorted()
    {
        return UNSORTED;
    }
    
    /**
     * Returns a new {@link Sort} with the current setup but descending order direction.
     * 
     * @return
     */
    public PageSort descending()
    {
        return withDirection(Direction.DESC);
    }
    
    /**
     * Returns a new {@link Sort} with the current setup but ascending order direction.
     * 
     * @return
     */
    public PageSort ascending()
    {
        return withDirection(Direction.ASC);
    }
    
    public boolean isSorted()
    {
        return !orders.isEmpty();
    }
    
    public boolean isUnsorted()
    {
        return !isSorted();
    }
    
    /**
     * Returns a new {@link Sort} consisting of the {@link Order}s of the current {@link Sort} combined with the given
     * ones.
     * 
     * @param sort must not be {@literal null}.
     * @return
     */
    public PageSort and(PageSort sort)
    {
        
        Assert.notNull(sort, "Sort must not be null!");
        
        ArrayList<Order> these = new ArrayList<>(this.orders);
        
        for (Order order : sort)
        {
            these.add(order);
        }
        
        return PageSort.by(these);
    }
    
    /**
     * Returns the order registered for the given property.
     * 
     * @param property
     * @return
     */
    @Nullable
    public Order getOrderFor(String property)
    {
        
        for (Order order : this)
        {
            if (order.getProperty().equals(property))
            {
                return order;
            }
        }
        
        return null;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Order> iterator()
    {
        return this.orders.iterator();
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(@Nullable Object obj)
    {
        
        if (this == obj)
        {
            return true;
        }
        
        if (!(obj instanceof PageSort))
        {
            return false;
        }
        
        PageSort that = (PageSort)obj;
        
        return this.orders.equals(that.orders);
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        
        int result = 17;
        result = 31 * result + orders.hashCode();
        return result;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return orders.isEmpty() ? "UNSORTED" : StringUtils.collectionToCommaDelimitedString(orders);
    }
    
    /**
     * Creates a new {@link Sort} with the current setup but the given order direction.
     * 
     * @param direction
     * @return
     */
    private PageSort withDirection(Direction direction)
    {
        
        return PageSort
            .by(orders.stream().map(it -> new Order(direction, it.getProperty())).collect(Collectors.toList()));
    }
    
    public Sort toSort()
    {
        if(orders == null)
            return Sort.unsorted();
        else
        {
            List<Sort.Order> orderlist = new ArrayList<>();
            for (Order o : orders)
            {
                orderlist.add(o.toSortOrder());
            }
            return Sort.by(orderlist);
        }
    }
    
    /**
     * PropertyPath implements the pairing of an {@link Direction} and a property. It is used to provide input for
     * {@link Sort}
     * 
     * @author Oliver Gierke
     * @author Kevin Raymond
     */
    public static class Order implements Serializable
    {
        
        private static final long serialVersionUID = 1522511010900108987L;
        
        private static final boolean DEFAULT_IGNORE_CASE = false;
        
        private static final NullHandling DEFAULT_NULL_HANDLING = NullHandling.NATIVE;
        
        private Direction direction;
        
        private String property;
        
        private boolean ignoreCase;
        
        private NullHandling nullHandling;
        
        public Order()
        {
            
        }
        
        /**
         * Creates a new {@link Order} instance. if order is {@literal null} then order defaults to
         * {@link Sort#DEFAULT_DIRECTION}
         * 
         * @param direction can be {@literal null}, will default to {@link Sort#DEFAULT_DIRECTION}
         * @param property must not be {@literal null} or empty.
         */
        public Order(@Nullable Direction direction, String property)
        {
            this(direction, property, DEFAULT_IGNORE_CASE, DEFAULT_NULL_HANDLING);
        }
        
        /**
         * Creates a new {@link Order} instance. if order is {@literal null} then order defaults to
         * {@link Sort#DEFAULT_DIRECTION}
         * 
         * @param direction can be {@literal null}, will default to {@link Sort#DEFAULT_DIRECTION}
         * @param property must not be {@literal null} or empty.
         * @param nullHandlingHint must not be {@literal null}.
         */
        public Order(@Nullable Direction direction, String property, NullHandling nullHandlingHint)
        {
            this(direction, property, DEFAULT_IGNORE_CASE, nullHandlingHint);
        }
        
        /**
         * Creates a new {@link Order} instance. Takes a single property. Direction defaults to
         * {@link Sort#DEFAULT_DIRECTION}.
         * 
         * @param property must not be {@literal null} or empty.
         * @since 2.0
         */
        public static Order by(String property)
        {
            return new Order(DEFAULT_DIRECTION, property);
        }
        
        /**
         * Creates a new {@link Order} instance. Takes a single property. Direction is {@link Direction#ASC} and
         * NullHandling {@link NullHandling#NATIVE}.
         *
         * @param property must not be {@literal null} or empty.
         * @since 2.0
         */
        public static Order asc(String property)
        {
            return new Order(Direction.ASC, property, DEFAULT_NULL_HANDLING);
        }
        
        /**
         * Creates a new {@link Order} instance. Takes a single property. Direction is {@link Direction#ASC} and
         * NullHandling {@link NullHandling#NATIVE}.
         *
         * @param property must not be {@literal null} or empty.
         * @since 2.0
         */
        public static Order desc(String property)
        {
            return new Order(Direction.DESC, property, DEFAULT_NULL_HANDLING);
        }
        
        /**
         * Creates a new {@link Order} instance. if order is {@literal null} then order defaults to
         * {@link Sort#DEFAULT_DIRECTION}
         * 
         * @param direction can be {@literal null}, will default to {@link Sort#DEFAULT_DIRECTION}
         * @param property must not be {@literal null} or empty.
         * @param ignoreCase true if sorting should be case insensitive. false if sorting should be case sensitive.
         * @param nullHandling must not be {@literal null}.
         * @since 1.7
         */
        private Order(@Nullable Direction direction, String property, boolean ignoreCase, NullHandling nullHandling)
        {
            
            if (!StringUtils.hasText(property))
            {
                throw new IllegalArgumentException("Property must not null or empty!");
            }
            
            this.direction = direction == null ? DEFAULT_DIRECTION : direction;
            this.property = property;
            this.ignoreCase = ignoreCase;
            this.nullHandling = nullHandling;
        }
        
        public Order(Sort.Order o)
        {
            this.direction = o.getDirection();
            this.property = o.getProperty();
            this.ignoreCase = o.isIgnoreCase();
            this.nullHandling = o.getNullHandling();
        }
        
        public Sort.Order toSortOrder()
        {
            Sort.Order o =
                new Sort.Order(direction, property, nullHandling);
            if (ignoreCase)
                return o.ignoreCase();
            else
                return o;
        }
        
        /**
         * Returns the order the property shall be sorted for.
         * 
         * @return
         */
        public Direction getDirection()
        {
            return direction;
        }
        
        /**
         * Returns the property to order for.
         * 
         * @return
         */
        public String getProperty()
        {
            return property;
        }
        
        /**
         * Returns whether sorting for this property shall be ascending.
         * 
         * @return
         */
        public boolean isAscending()
        {
            return this.direction.isAscending();
        }
        
        /**
         * Returns whether sorting for this property shall be descending.
         * 
         * @return
         * @since 1.13
         */
        public boolean isDescending()
        {
            return this.direction.isDescending();
        }
        
        /**
         * Returns whether or not the sort will be case sensitive.
         * 
         * @return
         */
        public boolean isIgnoreCase()
        {
            return ignoreCase;
        }
        
        /**
         * Returns a new {@link Order} with the given {@link Direction}.
         * 
         * @param direction
         * @return
         */
        public Order with(Direction direction)
        {
            return new Order(direction, this.property, this.ignoreCase, this.nullHandling);
        }
        
        /**
         * Returns a new {@link Order}
         * 
         * @param property must not be {@literal null} or empty.
         * @return
         * @since 1.13
         */
        public Order withProperty(String property)
        {
            return new Order(this.direction, property, this.ignoreCase, this.nullHandling);
        }
        
        /**
         * Returns a new {@link Sort} instance for the given properties.
         * 
         * @param properties
         * @return
         */
        public PageSort withProperties(String... properties)
        {
            return PageSort.by(this.direction, properties);
        }
        
        /**
         * Returns a new {@link Order} with case insensitive sorting enabled.
         * 
         * @return
         */
        public Order ignoreCase()
        {
            return new Order(direction, property, true, nullHandling);
        }
        
        /**
         * Returns a {@link Order} with the given {@link NullHandling}.
         * 
         * @param nullHandling can be {@literal null}.
         * @return
         * @since 1.8
         */
        public Order with(NullHandling nullHandling)
        {
            return new Order(direction, this.property, ignoreCase, nullHandling);
        }
        
        /**
         * Returns a {@link Order} with {@link NullHandling#NULLS_FIRST} as null handling hint.
         * 
         * @return
         * @since 1.8
         */
        public Order nullsFirst()
        {
            return with(NullHandling.NULLS_FIRST);
        }
        
        /**
         * Returns a {@link Order} with {@link NullHandling#NULLS_LAST} as null handling hint.
         * 
         * @return
         * @since 1.7
         */
        public Order nullsLast()
        {
            return with(NullHandling.NULLS_LAST);
        }
        
        /**
         * Returns a {@link Order} with {@link NullHandling#NATIVE} as null handling hint.
         * 
         * @return
         * @since 1.7
         */
        public Order nullsNative()
        {
            return with(NullHandling.NATIVE);
        }
        
        /**
         * Returns the used {@link NullHandling} hint, which can but may not be respected by the used datastore.
         * 
         * @return
         * @since 1.7
         */
        public NullHandling getNullHandling()
        {
            return nullHandling;
        }
        
        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode()
        {
            
            int result = 17;
            
            result = 31 * result + direction.hashCode();
            result = 31 * result + property.hashCode();
            result = 31 * result + (ignoreCase ? 1 : 0);
            result = 31 * result + nullHandling.hashCode();
            
            return result;
        }
        
        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(@Nullable Object obj)
        {
            
            if (this == obj)
            {
                return true;
            }
            
            if (!(obj instanceof Order))
            {
                return false;
            }
            
            Order that = (Order)obj;
            
            return this.direction.equals(that.direction) && this.property.equals(that.property)
                && this.ignoreCase == that.ignoreCase && this.nullHandling.equals(that.nullHandling);
        }
        
        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            
            String result = String.format("%s: %s", property, direction);
            
            if (!NullHandling.NATIVE.equals(nullHandling))
            {
                result += ", " + nullHandling;
            }
            
            if (ignoreCase)
            {
                result += ", ignoring case";
            }
            
            return result;
        }
    }
}
