import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { getUserProfile } from '../api/userService';
import { useParams } from 'react-router-dom';
import { CircularProgress, Typography, Card, CardContent } from '@mui/material';

const UserProfile: React.FC = () => {
  const { userId } = useParams<{ userId: string }>();

  const { data: user, error, isLoading } = useQuery({
    queryKey: ['user', userId],
    queryFn: () => getUserProfile(userId!),
    enabled: !!userId,
  });

  if (isLoading) {
    return <CircularProgress />;
  }

  if (error) {
    return <Typography color="error">Failed to load user profile.</Typography>;
  }

  if (!user) {
    return <Typography>User not found.</Typography>;
  }

  return (
    <Card>
      <CardContent>
        <Typography variant="h5">User Profile</Typography>
        <Typography><strong>Email:</strong> {user.email}</Typography>
        <Typography><strong>First Name:</strong> {user.firstName}</Typography>
        <Typography><strong>Last Name:</strong> {user.lastName}</Typography>
        <Typography><strong>Phone:</strong> {user.phone}</Typography>
        <Typography><strong>Status:</strong> {user.status}</Typography>
      </CardContent>
    </Card>
  );
};

export default UserProfile;
