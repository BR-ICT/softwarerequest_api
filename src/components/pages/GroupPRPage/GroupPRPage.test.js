import React from 'react';
import { shallow } from 'enzyme';
import GroupPRPage from './GroupPRPage';

describe('<GroupPRPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<GroupPRPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
