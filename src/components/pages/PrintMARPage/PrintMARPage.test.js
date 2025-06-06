import React from 'react';
import { shallow } from 'enzyme';
import PrintMARPage from './PrintMARPage';

describe('<PrintMARPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<PrintMARPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
