import React from 'react';
import { shallow } from 'enzyme';
import ConfirmMARPage from './ConfirmMARPage';

describe('<ConfirmMARPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<ConfirmMARPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
