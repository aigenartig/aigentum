import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { getCart } from '../api/orderService';
import { useParams } from 'react-router-dom';
import { CircularProgress, Typography, Card, CardContent, List, ListItem, ListItemText } from '@mui/material';

const ShoppingCart: React.FC = () => {
  const { userId } = useParams<{ userId: string }>();

  const { data: cart, error, isLoading } = useQuery({
    queryKey: ['cart', userId],
    queryFn: () => getCart(userId!),
    enabled: !!userId,
  });

  if (isLoading) {
    return <CircularProgress />;
  }

  if (error) {
    return <Typography color="error">Failed to load shopping cart.</Typography>;
  }

  if (!cart) {
    return <Typography>Shopping cart not found.</Typography>;
  }

  return (
    <Card>
      <CardContent>
        <Typography variant="h5">Shopping Cart</Typography>
        <Typography><strong>Order Number:</strong> {cart.orderNumber}</Typography>
        <Typography><strong>Status:</strong> {cart.status}</Typography>
        <Typography><strong>Total:</strong> {cart.totalAmount} {cart.currency}</Typography>
        <List>
          {cart.items.map((item) => (
            <ListItem key={item.id}>
              <ListItemText
                primary={`Product ID: ${item.productId}`}
                secondary={`Quantity: ${item.quantity} - Price: ${item.totalPrice}`}
              />
            </ListItem>
          ))}
        </List>
      </CardContent>
    </Card>
  );
};

export default ShoppingCart;
