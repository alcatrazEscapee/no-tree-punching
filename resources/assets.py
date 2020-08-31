#  Part of the No Tree Punching mod by AlcatrazEscapee.
#  Work under copyright. See the project LICENSE.md for details.

from mcresources import ResourceManager
from mcresources.block_context import BlockContext


def generate(rm: ResourceManager):
    # First
    rm.lang({
        'itemGroup.notreepunching.items': 'No Tree Punching',
        'notreepunching.tooltip.small_vessel_more': '%d More...'
    })

    # Stone
    for stone in ('granite', 'andesite', 'diorite'):
        block = rm.blockstate('%s_cobblestone' % stone) \
            .with_block_model() \
            .with_item_model() \
            .with_tag('cobblestone') \
            .with_block_loot('notreepunching:%s_cobblestone' % stone) \
            .with_lang(lang('%s cobblestone', stone)) \
            .make_stairs() \
            .make_slab()
        wall_v2(block)  # todo: for now. we use this as mcresources is not updated for the 1.16.x pack version change here
        for piece in ('stairs', 'slab', 'wall'):
            rm.block('%s_cobblestone_%s' % (stone, piece)) \
                .with_lang(lang('%s cobblestone %s', stone, piece)) \
                .with_tag('minecraft:' + piece + ('s' if not piece.endswith('s') else ''))  # plural tag

    for stone in ('granite', 'andesite', 'diorite', 'stone', 'sandstone', 'red_sandstone'):
        rm.blockstate('%s_loose_rock' % stone) \
            .with_block_model(textures='minecraft:block/%s' % stone, parent='notreepunching:block/loose_rock') \
            .with_block_loot('notreepunching:%s_loose_rock' % stone) \
            .with_lang(lang('%s loose rock', stone))
        # flat item model for the block item
        rm.item_model('%s_loose_rock' % stone) \
            .with_tag('loose_rocks')  # item tag is needed for recipes

    # Pottery
    for pottery in ('worked', 'large_vessel', 'small_vessel', 'bucket', 'flower_pot'):
        block = rm.blockstate('clay_%s' % pottery) \
            .with_block_model(textures='minecraft:block/clay', parent='notreepunching:block/pottery_%s' % pottery) \
            .with_item_model() \
            .with_block_loot('notreepunching:clay_%s' % pottery)
        if pottery == 'worked':
            block.with_lang(lang('worked clay'))
        else:
            block.with_lang(lang('clay %s', pottery))

    rm.blockstate('ceramic_large_vessel') \
        .with_block_model(textures='notreepunching:block/ceramic', parent='notreepunching:block/pottery_large_vessel') \
        .with_item_model() \
        .with_block_loot({
        'entries': {
            'name': 'notreepunching:ceramic_large_vessel',
            'functions': [
                {
                    'function': 'minecraft:copy_name',
                    'source': 'block_entity'
                }, {
                    'function': 'minecraft:copy_nbt',
                    'source': 'block_entity',
                    'ops': [{
                        'source': '',
                        'target': 'BlockEntityTag',
                        'op': 'replace'
                    }]
                }
            ],
        }
    }) \
        .with_lang(lang('ceramic large vessel'))

    # Tools
    for tool in ('iron', 'gold', 'diamond'):
        rm.item_model('%s_mattock' % tool) \
            .with_lang(lang('%s mattock', tool))
        rm.item_model('%s_saw' % tool) \
            .with_lang(lang('%s saw', tool)) \
            .with_tag('saws')
        rm.item_model('%s_knife' % tool) \
            .with_lang(lang('%s knife', tool)) \
            .with_tag('knives')

    # Flint
    for tool in ('axe', 'pickaxe', 'shovel', 'hoe', 'knife'):
        rm.item_model('flint_%s' % tool) \
            .with_lang(lang('flint %s', tool))
    rm.item_model('macuahuitl') \
        .with_lang(lang('macuahuitl'))
    rm.item('flint_knife').with_tag('knives')

    for item in ('flint_shard', 'plant_fiber', 'plant_string', 'clay_brick', 'ceramic_small_vessel', 'clay_tool', 'fire_starter'):
        rm.item_model(item) \
            .with_lang(lang(item))

    # ceramic bucket, since it uses a very custom model
    rm.data(('models', 'item', 'ceramic_bucket'), {
        'parent': 'forge:item/default',
        'textures': {
            'base': 'notreepunching:item/ceramic_bucket',
            'fluid': 'forge:item/mask/bucket_fluid_drip'
        },
        'loader': 'forge:bucket',
        'fluid': 'empty'
    }, root_domain='assets')
    rm.item('ceramic_bucket').with_lang(lang('ceramic bucket'))

    # Misc Tags
    rm.item('plant_string').with_tag('forge:string')
    rm.block('minecraft:gravel').with_tag('always_breakable').with_tag('always_drops')
    for wood in ('acacia', 'oak', 'dark_oak', 'jungle', 'birch', 'spruce'):
        rm.block('minecraft:%s_leaves' % wood).with_tag('always_breakable').with_tag('always_drops')

    rm.item_tag('fire_starter_logs', '#minecraft:logs', '#minecraft:planks')
    rm.item_tag('fire_starter_kindling', '#forge:rods/wooden', '#minecraft:saplings', '#minecraft:leaves', '#forge:string', 'notreepunching:plant_fiber')

    rm.item('ceramic_small_vessel').with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')
    rm.item('ceramic_large_vessel').with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')
    rm.item('minecraft:shulker_box').with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')
    for color in ('white', 'orange', 'magenta', 'light_blue', 'yellow', 'lime', 'pink', 'gray', 'light_gray', 'cyan', 'purple', 'blue', 'brown', 'green', 'red', 'black'):
        rm.item('minecraft:%s_shulker_box' % color).with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')


def generate_vanilla(rm: ResourceManager):
    # Vanilla Tags
    rm.item('flint').with_tag('notreepunching:flint_knappable')
    for block in ('grass_block', 'dirt', 'coarse_dirt', 'gravel', 'sand', 'red_sand', 'terracotta', 'stone', 'andesite', 'diorite', 'granite', 'sandstone', 'red_sandstone', 'podzol'):
        rm.block(block).with_tag('notreepunching:loose_rock_placeable_on')


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()


def wall_v2(ctx: BlockContext, wall_suffix: str = '_wall'):
    block = ctx.res.join('block/')
    wall = ctx.res.join() + wall_suffix
    wall_post = block + wall_suffix + '_post'
    wall_side = block + wall_suffix + '_side'
    wall_side_tall = block + wall_suffix + '_side_tall'
    wall_inv = block + wall_suffix + '_inventory'
    ctx.rm.blockstate_multipart(wall, wall_multipart_v2(wall_post, wall_side, wall_side_tall))
    ctx.rm.block_model(wall + '_post', textures={'wall': block}, parent='block/template_wall_post')
    ctx.rm.block_model(wall + '_side', textures={'wall': block}, parent='block/template_wall_side')
    ctx.rm.block_model(wall + '_side_tall', textures={'wall': block}, parent='block/template_wall_side_tall')
    ctx.rm.block_model(wall + '_inventory', textures={'wall': block}, parent='block/wall_inventory')
    ctx.rm.item_model(wall, parent=wall_inv, no_textures=True)


def wall_multipart_v2(wall_post: str, wall_side: str, wall_side_tall: str):
    return [
        ({'up': 'true'}, {'model': wall_post}),
        ({'north': 'low'}, {'model': wall_side, 'uvlock': True}),
        ({'east': 'low'}, {'model': wall_side, 'y': 90, 'uvlock': True}),
        ({'south': 'low'}, {'model': wall_side, 'y': 180, 'uvlock': True}),
        ({'west': 'low'}, {'model': wall_side, 'y': 270, 'uvlock': True}),
        ({'north': 'tall'}, {'model': wall_side_tall, 'uvlock': True}),
        ({'east': 'tall'}, {'model': wall_side_tall, 'y': 90, 'uvlock': True}),
        ({'south': 'tall'}, {'model': wall_side_tall, 'y': 180, 'uvlock': True}),
        ({'west': 'tall'}, {'model': wall_side_tall, 'y': 270, 'uvlock': True})
    ]
