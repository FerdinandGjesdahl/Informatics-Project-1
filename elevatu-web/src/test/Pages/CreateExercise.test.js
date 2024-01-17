import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';

import '@testing-library/jest-dom';
import CreateExercise from '../src/pages/createExercise/CreateExercise';

// Mocking DataSource
jest.mock('../../helpers/DataSource.js', () => ({
  addExercise: jest.fn(),
}));

// Mocking universal-cookie
jest.mock('universal-cookie', () => ({
  __esModule: true,
  default: jest.fn(),
}));

describe('CreateExercise Component', () => {
  it('renders correctly', () => {
    render(<CreateExercise />);
    
    expect(screen.getByText('Create Exercise')).toBeInTheDocument();
  });

  it('handles input changes', () => {
    render(<CreateExercise />);

    const exerciseNameInput = screen.getByLabelText('Exercise Name:');
    const setsInput = screen.getByLabelText('Sets:');
    const repsInput = screen.getByLabelText('Reps:');
    const notesField = screen.getByLabelText('Notes:');

    fireEvent.change(exerciseNameInput, { target: { value: 'Test Exercise' } });
    fireEvent.change(setsInput, { target: { value: '5' } });
    fireEvent.change(repsInput, { target: { value: '10' } });
    fireEvent.change(notesField, { target: { value: 'Test Notes' } });

    expect(exerciseNameInput.value).toBe('Test Exercise');
    expect(setsInput.value).toBe('5');
    expect(repsInput.value).toBe('10');
    expect(notesField.value).toBe('Test Notes');
  });

  it('handles number input for Sets and Reps', () => {
    render(<CreateExercise />);

    const setsInput = screen.getByLabelText('Sets:');
    const repsInput = screen.getByLabelText('Reps:');

    fireEvent.change(setsInput, { target: { value: 'abc123' } });
    fireEvent.change(repsInput, { target: { value: 'xyz456' } });

    expect(setsInput.value).toBe('123');
    expect(repsInput.value).toBe('456');
  });

  it('prevents non-numeric input for Sets and Reps', () => {
    render(<CreateExercise />);

    const setsInput = screen.getByLabelText('Sets:');
    const repsInput = screen.getByLabelText('Reps:');

    fireEvent.keyPress(setsInput, { key: 'a' });
    fireEvent.keyPress(repsInput, { key: 'b' });

    expect(setsInput.value).toBe('');
    expect(repsInput.value).toBe('');
  });

  it('handles the click event for the "Create Exercise" button', async () => {
    render(<CreateExercise />);

    const createExerciseButton = screen.getByText('Create Exercise');
    fireEvent.click(createExerciseButton);


  });
});
