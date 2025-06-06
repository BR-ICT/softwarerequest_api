import React from 'react';
import { shallow } from 'enzyme';
import GenPOPage from './GenPOPage';

describe('<GenPOPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<GenPOPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
