import HeroSection from '../components/HeroSection';
import WhyUse from '../components/WhyUse';
import FeaturedTrees from '../components/FeaturedTrees';
import StatsSection from '../components/StatsSection';

export default function HomePage({ trees }) {
  return (
    <>
      <HeroSection />
      <WhyUse />
      <FeaturedTrees trees={trees} />
      <StatsSection />
    </>
  );
}
