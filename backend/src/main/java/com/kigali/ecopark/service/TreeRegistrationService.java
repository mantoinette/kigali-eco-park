package com.kigali.ecopark.service;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.repository.TreeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Registers a new tree: downloads or discovers images, stores them locally,
 * and persists the tree profile.
 */
@Service
public class TreeRegistrationService {

    private final TreeRepository treeRepository;
    private final TreeImageAcquisitionService imageAcquisitionService;

    public TreeRegistrationService(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService
    ) {
        this.treeRepository = treeRepository;
        this.imageAcquisitionService = imageAcquisitionService;
    }

    @Transactional
    public Tree registerTree(
            Tree tree,
            List<TreeImageAcquisitionService.ImageRequest> imageSources
    ) {
        List<TreeImageAcquisitionService.AcquiredImage> acquired = imageAcquisitionService.acquireImages(
                tree.getSlug(),
                tree.getScientificName(),
                imageSources
        );

        tree.getImages().clear();
        for (TreeImageAcquisitionService.AcquiredImage image : acquired) {
            TreeImage treeImage = new TreeImage();
            treeImage.setTree(tree);
            treeImage.setUrl(image.publicUrl());
            treeImage.setCaption(image.caption());
            treeImage.setPrimary(image.primary());
            treeImage.setDisplayOrder(image.displayOrder());
            tree.getImages().add(treeImage);
        }

        return treeRepository.save(tree);
    }
}
