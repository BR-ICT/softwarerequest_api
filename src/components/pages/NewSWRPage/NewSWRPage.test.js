import React from 'react';
import { shallow } from 'enzyme';
import NewSWRPage from './NewSWRPage';

describe('<NewSWRPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<NewSWRPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
